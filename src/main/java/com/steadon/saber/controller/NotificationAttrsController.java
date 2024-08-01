package com.steadon.saber.controller;

import com.steadon.saber.biz.INotificationAttrsBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.param.FeishuAttrsParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 升级参数管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class NotificationAttrsController {

    private final INotificationAttrsBiz iNotificationAttrsBiz;

    /**
     * 获得飞书升级参数
     */
    @GetMapping("/feishu/attrs")
    public SaberResponse<NotificationFeishuAttrs> getFeishuAttrs(@RequestParam(value = "id", required = false) Integer ruleId) {
        return iNotificationAttrsBiz.queryFeishuAttrs(ruleId);
    }

    /**
     * 更新飞书升级参数
     */
    @PostMapping("/feishu/attrs")
    public SaberResponse<String> updateFeishuAttrs(@RequestBody FeishuAttrsParam param) {
        return iNotificationAttrsBiz.updateFeishuAttrs(param);
    }
}