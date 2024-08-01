package com.steadon.saber.client.feishu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FeishuNotificationRequest implements Serializable {
    // 飞书机器人
    private String feishuAppId;
    private String feishuAppSecret;

    // 推送消息标识
    private String chatId;
    private List<String> userIdList;

    // 推送消息主体
    private String title;
    private String content;

    // 推送消息类型
    private String templateId;
    private String messageType;
    private Object templateArgs;
}
