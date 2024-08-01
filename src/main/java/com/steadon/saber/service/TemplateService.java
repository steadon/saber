package com.steadon.saber.service;

import com.steadon.saber.model.dao.Template;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchTemplateParam;

import java.util.Map;

public interface TemplateService {

    Template queryOne(Integer templateId);

    Template queryOne(String name);

    PageData<Template> queryList(SearchTemplateParam param, int pageNo, int pageSize);

    void createOne(Template template);

    void updateOne(Template template);

    void deleteOne(Template template);

    String handleTemplate(Integer templateId, Map<String, String> params);

    String handleTemplate(String templateContent, UserInfo userInfo);
}
