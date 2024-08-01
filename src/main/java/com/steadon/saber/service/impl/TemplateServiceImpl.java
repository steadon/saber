package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.annotation.AppIsolated;
import com.steadon.saber.common.AdminField;
import com.steadon.saber.enums.PreParam;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.exception.SaberInvalidException;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.Template;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchTemplateParam;
import com.steadon.saber.repository.mapper.NotificationRuleMapper;
import com.steadon.saber.repository.mapper.TemplateMapper;
import com.steadon.saber.service.TemplateService;
import com.steadon.saber.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;
    private final NotificationRuleMapper notificationRuleMapper;

    private final Pattern SMS_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    private final Pattern PRE_PATTERN = Pattern.compile("\\#\\{([^}]+)\\}");
    private final Pattern COM_PATTERN = Pattern.compile("(?<![$#])\\{([^}]+)\\}");
    private final Pattern HTML_PATTERN = Pattern.compile("<html>(.*?)</html>", Pattern.DOTALL);
    private final Pattern BODY_PATTERN = Pattern.compile("<body>(.*?)</body>", Pattern.DOTALL);

    @Override
    public Template queryOne(Integer templateId) {
        return templateMapper.selectById(templateId);
    }

    @Override
    public Template queryOne(String name) {
        return templateMapper.selectOne(new QueryWrapper<Template>().eq("name", name));
    }

    @Override
    public void createOne(Template template) {
        if (templateMapper.insert(template) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateOne(Template template) {
        if (templateMapper.updateById(template) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteOne(Template template) {
        // 存在依赖当前模板的消息规则
        Long count = notificationRuleMapper.selectCount(new QueryWrapper<NotificationRule>().eq("template_id", template.getId()));
        if (count != 0) {
            throw new SaberInvalidException("模版已经被使用在 " + count + "个规则中");
        }
        template.setUpdateBy(TokenHandler.getUsername());
        if (templateMapper.updateById(template) == 0) {
            throw new SaberDBException();
        }
        if (templateMapper.deleteById(template) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    @AppIsolated
    public PageData<Template> queryList(SearchTemplateParam param, int pageNo, int pageSize) {
        QueryWrapper<Template> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        String keyword = param.getKeyword();
        String createBy = param.getCreateBy();
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like("name", keyword)
                    .or().like("content", keyword)
                    .or().like("description", keyword);
        }
        if (StringUtils.isNotEmpty(createBy)) {
            wrapper.eq("create_by", createBy);
        }
        if (param.getFeishuStatus()) {
            wrapper.eq("feishu_status", 1);
        }
        if (param.getEmailStatus()) {
            wrapper.eq("email_status", 1);
        }
        if (param.getSmsStatus()) {
            wrapper.eq("sms_status", 1);
        }
        Page<Template> page = new Page<>(pageNo, pageSize);
        Page<Template> pageData = templateMapper.selectPage(page, wrapper);
        return new PageData<Template>().praise(pageData);
    }

    @Override
    public String handleTemplate(Integer templateId, Map<String, String> params) {
        // 检查模板是否存在
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            return null;
        }
        String templateContent = template.getContent();

        // 检测是否属于HTML模板
        Matcher matcher = HTML_PATTERN.matcher(templateContent);
        if (matcher.find()) {
            return handleHtmlTemplate(templateContent, params);
        }
        // 检测是否属于SMS的模板
        matcher = SMS_PATTERN.matcher(templateContent);
        if (matcher.find()) {
            return handleSmsTemplate(matcher, params);
        }
        // 替换非短信消息占位符
        return replacePlaceholders(templateContent, COM_PATTERN, params);
    }

    @Override
    public String handleTemplate(String templateContent, UserInfo userInfo) {
        return replacePlaceholders(templateContent, PRE_PATTERN, key -> getValue(userInfo, key));
    }

    public String handleHtmlTemplate(String templateContent, Map<String, String> params) {
        if (templateContent == null) {
            return StringUtils.EMPTY;
        }
        Matcher matcher = BODY_PATTERN.matcher(templateContent);
        if (matcher.find()) {
            String bodyContent = matcher.group(1);
            String replacedBodyContent = replacePlaceholders(bodyContent, COM_PATTERN, params);

            // 重建HTML字符串
            return "%s%s%s".formatted(
                    templateContent.substring(0, matcher.start(1)),
                    replacedBodyContent,
                    templateContent.substring(matcher.end(1))
            );
        }
        return templateContent;
    }

    private String handleSmsTemplate(Matcher matcher, Map<String, String> params) {
        StringJoiner result = new StringJoiner(", ", "{", "}");

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = params.get(key);
            throwIfParamIsNull(value, key);
            result.add(String.format("\"%s\":\"%s\"", key, value));
        }
        return result.toString();
    }

    private String replacePlaceholders(String templateContent, Pattern pattern, Map<String, String> params) {
        return replacePlaceholders(templateContent, pattern, params::get);
    }

    private String replacePlaceholders(String templateContent, Pattern pattern, PlaceholderResolver resolver) {
        if (templateContent == null) {
            return StringUtils.EMPTY;
        }
        Matcher matcher = pattern.matcher(templateContent);
        if (matcher.find()) {
            matcher.reset();
            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String key = matcher.group(1);
                String value = resolver.resolve(key);
                throwIfParamIsNull(value, key);
                matcher.appendReplacement(result, value);
            }
            matcher.appendTail(result);
            return result.toString();
        }
        return templateContent;
    }

    private interface PlaceholderResolver {
        String resolve(String key);
    }

    private String getValue(UserInfo userInfo, String key) {
        if (PreParam.name.name().equals(key)) {
            return userInfo.getName();
        } else if (PreParam.department.name().equals(key)) {
            return userInfo.getDepartment();
        } else if (PreParam.email.name().equals(key)) {
            return userInfo.getEmail();
        } else if (PreParam.mobile.name().equals(key)) {
            return userInfo.getMobile();
        } else if (PreParam.userId.name().equals(key)) {
            return userInfo.getUserId();
        } else if (PreParam.appId.name().equals(key)) {
            return userInfo.getAppId();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 抛出异常的方法，如果参数为null
     */
    private void throwIfParamIsNull(String value, String key) {
        if (value == null) {
            throw new SaberInvalidException("缺少模版参数: " + key);
        }
    }
}