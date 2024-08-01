package com.steadon.saber.controller;

import com.steadon.saber.biz.ITemplateBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Template;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchTemplateParam;
import com.steadon.saber.model.param.TemplateParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 模版管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class TemplateController {

    private final ITemplateBiz iTemplateBiz;

    /**
     * 新增消息模版
     */
    @PostMapping("/template/add")
    public SaberResponse<Template> addTemplate(@RequestBody TemplateParam param) {
        return iTemplateBiz.addTemplate(param);
    }

    /**
     * 更新消息模版
     */
    @PostMapping("/template/update")
    public SaberResponse<Template> updateTemplate(@RequestParam(value = "id") int templateId,
                                                  @RequestBody TemplateParam param) {
        return iTemplateBiz.updateTemplate(templateId, param);
    }

    /**
     * 删除消息模版
     */
    @PostMapping("/template/delete")
    public SaberResponse<Void> deleteTemplate(@RequestParam(value = "id") int templateId) {
        return iTemplateBiz.deleteTemplate(templateId);
    }

    /**
     * 分页获取消息模版
     */
    @PostMapping("/template/list")
    public SaberResponse<PageData<Template>> getTemplateList(@RequestBody SearchTemplateParam param,
                                                             @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iTemplateBiz.getTemplateList(param, pageNo, pageSize);
    }

    /**
     * 分页获取消息模版
     */
    @GetMapping("/template")
    public SaberResponse<Template> getTemplate(@RequestParam(value = "id") int templateId) {
        return iTemplateBiz.getTemplate(templateId);
    }
}
