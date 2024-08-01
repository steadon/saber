package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Template;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchTemplateParam;
import com.steadon.saber.model.param.TemplateParam;

public interface ITemplateBiz {

    SaberResponse<Template> addTemplate(TemplateParam param);

    SaberResponse<Template> updateTemplate(int templateId, TemplateParam param);

    SaberResponse<Void> deleteTemplate(int templateId);

    SaberResponse<PageData<Template>> getTemplateList(SearchTemplateParam param, int pageNo, int pageSize);

    SaberResponse<Template> getTemplate(int templateId);
}