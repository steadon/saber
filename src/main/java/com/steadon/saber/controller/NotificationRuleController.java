package com.steadon.saber.controller;

import com.steadon.saber.biz.INotificationRuleBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationRuleParam;
import com.steadon.saber.model.param.SearchNotificationRuleParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 规则管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class NotificationRuleController {

    private final INotificationRuleBiz iNotificationRuleBiz;

    /**
     * 新增通知规则
     */
    @PostMapping("/notification/rule/add")
    public SaberResponse<NotificationRule> addNotificationRule(@RequestBody NotificationRuleParam param) {
        return iNotificationRuleBiz.addNotificationRule(param);
    }

    /**
     * 更新通知规则
     */
    @PostMapping("/notification/rule/update")
    public SaberResponse<String> updateNotificationRule(@RequestParam(value = "id") int ruleId,
                                                        @RequestBody NotificationRuleParam param) {
        return iNotificationRuleBiz.updateNotificationRule(ruleId, param);
    }

    /**
     * 删除通知规则
     */
    @PostMapping("/notification/rule/delete")
    public SaberResponse<String> deleteNotificationRule(@RequestParam(value = "id") int ruleId) {
        return iNotificationRuleBiz.deleteNotificationRule(ruleId);
    }

    /**
     * 分页获取规则
     */
    @PostMapping("/notification/rule/list")
    public SaberResponse<PageData<NotificationRuleDto>> getNotificationRuleList(@RequestBody SearchNotificationRuleParam param,
                                                                                @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                                                @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iNotificationRuleBiz.getNotificationRuleList(param, pageNo, pageSize);
    }
}
