package com.steadon.saber.service;

import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.dao.NotificationSmsAttrs;

public interface NotificationAttrsService {

    NotificationFeishuAttrs queryFeishuAttrs(Integer ruleId);

    NotificationSmsAttrs querySmsAttrs(int ruleId);

    void addSmsAttrs(NotificationSmsAttrs smsAttrs);

    void deleteSmsAttrs(int ruleId);

    void addFeishuAttrs(NotificationFeishuAttrs feishuAttrs);

    void deleteFeishuAttrs(int ruleId);

    void updateFeishuAttrs(NotificationFeishuAttrs feishuAttrs);
}
