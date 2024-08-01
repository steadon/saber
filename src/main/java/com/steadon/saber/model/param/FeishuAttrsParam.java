package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class FeishuAttrsParam {
    private Integer ruleId;
    private String chatId;
    private String templateId;
    private String feishuAppId;
    private String messageType;
}