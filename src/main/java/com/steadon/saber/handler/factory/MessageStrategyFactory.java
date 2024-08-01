package com.steadon.saber.handler.factory;

import com.steadon.saber.biz.chain.model.MessageStrategy;
import com.steadon.saber.exception.SaberBaseException;
import com.steadon.saber.model.dao.NotificationRule;
import org.springframework.stereotype.Component;

@Component
public class MessageStrategyFactory {

    public MessageStrategy createStrategy(NotificationRule notificationRule) {
        if (notificationRule == null) {
            throw new SaberBaseException("通知规则不能为空", new IllegalArgumentException());
        }
        return new MessageStrategy(notificationRule.getFeishuStatus(), notificationRule.getSmsStatus(), notificationRule.getEmailStatus());
    }
}
