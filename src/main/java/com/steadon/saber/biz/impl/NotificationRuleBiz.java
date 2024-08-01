package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.INotificationRuleBiz;
import com.steadon.saber.handler.helper.AuthHelper;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.NotificationSmsAttrs;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationRuleParam;
import com.steadon.saber.model.param.SearchNotificationRuleParam;
import com.steadon.saber.service.NotificationAttrsService;
import com.steadon.saber.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationRuleBiz implements INotificationRuleBiz {

    private final NotificationService notificationService;
    private final NotificationAttrsService notificationAttrsService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SaberResponse<NotificationRule> addNotificationRule(NotificationRuleParam param) {
        String appId = TokenHandler.getAppId();

        // 确保规则Code的唯一性
        if (notificationService.verifyRuleCode(param.getCode()) != null) {
            return SaberResponse.failure("ruleCode已经存在");
        }
        // 加载参数创建通知规则
        String username = TokenHandler.getUsername();
        NotificationRule notificationRule = new NotificationRule();
        BeanUtils.copyProperties(param, notificationRule);
        notificationRule.setAppId(appId);
        notificationRule.setCreateBy(username);
        notificationRule.setUpdateBy(username);
        notificationService.addRule(notificationRule);
        log.info("addedNotificationRule: notificationRule={}", notificationRule);
        // 新建飞书升级参数
        NotificationFeishuAttrs feishuAttrs = new NotificationFeishuAttrs();
        feishuAttrs.setRuleId(notificationRule.getId());
        feishuAttrs.setCreateBy(username);
        feishuAttrs.setUpdateBy(username);
        notificationAttrsService.addFeishuAttrs(feishuAttrs);
        log.info("addedFeishuAttrs: feishuAttrs={}", feishuAttrs);
        // 新建短信升级参数
        NotificationSmsAttrs smsAttrs = new NotificationSmsAttrs();
        smsAttrs.setRuleId(notificationRule.getId());
        smsAttrs.setCreateBy(username);
        smsAttrs.setUpdateBy(username);
        notificationAttrsService.addSmsAttrs(smsAttrs);
        log.info("addedSmsAttrs: smsAttrs={}", smsAttrs);

        return SaberResponse.success(notificationRule);
    }

    @Override
    public SaberResponse<String> updateNotificationRule(int ruleId, NotificationRuleParam param) {
        // 检查规则的存在性
        NotificationRule notificationRule = notificationService.queryRuleById(ruleId);
        AuthHelper.checkPermission(notificationRule.getAppId());
        // 确保规则Code的唯一性
        if (!notificationRule.getCode().equals(param.getCode())) {
            if (!notificationService.checkRuleCode(param.getCode())) {
                return SaberResponse.failure("ruleCode已经存在");
            }
        }
        BeanUtils.copyProperties(param, notificationRule);
        notificationRule.setId(ruleId);
        notificationRule.setUpdateBy(TokenHandler.getUsername());
        notificationService.updateRule(notificationRule);
        log.info("updatedNotificationRule: notificationRule={}", notificationRule);
        return SaberResponse.success();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SaberResponse<String> deleteNotificationRule(int ruleId) {
        // 此处已经证实rule的存在性
        NotificationRule notificationRule = notificationService.queryRuleById(ruleId);
        AuthHelper.checkPermission(notificationRule.getAppId());
        // 删除飞书升级参数
        if (notificationRule.getFeishuStatus()) {
            notificationAttrsService.deleteFeishuAttrs(ruleId);
        }
        // 删除短信升级参数
        if (notificationRule.getSmsStatus()) {
            notificationAttrsService.deleteSmsAttrs(ruleId);
        }
        notificationService.deleteRule(ruleId);
        log.info("deletedNotificationRule: ruleId={}", ruleId);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<PageData<NotificationRuleDto>> getNotificationRuleList(SearchNotificationRuleParam param, int pageNo, int pageSize) {
        PageData<NotificationRuleDto> pageData = notificationService.queryRuleList(param, pageNo, pageSize);
        return SaberResponse.success(pageData);
    }
}
