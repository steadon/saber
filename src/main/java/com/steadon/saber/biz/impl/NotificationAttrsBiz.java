package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.INotificationAttrsBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.param.FeishuAttrsParam;
import com.steadon.saber.service.NotificationAttrsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationAttrsBiz implements INotificationAttrsBiz {

    @Resource
    private NotificationAttrsService notificationAttrsService;

    @Override
    public SaberResponse<String> updateFeishuAttrs(FeishuAttrsParam param) {
        NotificationFeishuAttrs feishuAttrs = new NotificationFeishuAttrs();
        BeanUtils.copyProperties(param, feishuAttrs);
        feishuAttrs.setUpdateBy(TokenHandler.getUsername());
        notificationAttrsService.updateFeishuAttrs(feishuAttrs);
        log.info("updatedFeishuAttrs: feishuAttrs={}", feishuAttrs);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<NotificationFeishuAttrs> queryFeishuAttrs(Integer ruleId) {
        NotificationFeishuAttrs feishuAttrs = notificationAttrsService.queryFeishuAttrs(ruleId);
        return SaberResponse.success(feishuAttrs);
    }
}