package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.ITemplateBiz;
import com.steadon.saber.handler.helper.AuthHelper;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Template;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchTemplateParam;
import com.steadon.saber.model.param.TemplateParam;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplateBiz implements ITemplateBiz {

    @Resource
    private TemplateService templateService;

    @Override
    public SaberResponse<Template> addTemplate(TemplateParam param) {
        String appId = TokenHandler.getAppId();
        String username = TokenHandler.getUsername();

        if (templateService.queryOne(param.getName()) != null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "模版名不能重复");
        }
        Template template = new Template();
        BeanUtils.copyProperties(param, template);
        template.setAppId(appId);
        template.setCreateBy(username);
        template.setUpdateBy(username);
        templateService.createOne(template);
        log.info("addedTemplate: template={}", template);
        return SaberResponse.success(template);
    }

    @Override
    public SaberResponse<Template> updateTemplate(int templateId, TemplateParam param) {
        Template template = templateService.queryOne(templateId);
        if (template == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "模版不存在");
        }
        AuthHelper.checkPermission(template.getAppId());
        BeanUtils.copyProperties(param, template);
        template.setUpdateBy(TokenHandler.getUsername());
        templateService.updateOne(template);
        log.info("updatedTemplate: template={}", template);
        return SaberResponse.success(template);
    }

    @Override
    public SaberResponse<Void> deleteTemplate(int templateId) {
        Template template = templateService.queryOne(templateId);
        if (template == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "模版不存在");
        }
        AuthHelper.checkPermission(template.getAppId());
        templateService.deleteOne(template);
        log.info("deletedTemplate: template={}", template);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<PageData<Template>> getTemplateList(SearchTemplateParam param, int pageNo, int pageSize) {
        PageData<Template> templateListResult = templateService.queryList(param, pageNo, pageSize);
        return SaberResponse.success(templateListResult);
    }

    @Override
    public SaberResponse<Template> getTemplate(int templateId) {
        Template template = templateService.queryOne(templateId);
        return SaberResponse.success(template);
    }
}
