package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class NotificationRuleParam {
    private String code;
    private String description;
    private Integer uid;
    private Integer groupId;
    private Integer type;
    private Integer templateId;
    private Boolean feishuStatus;
    private Boolean emailStatus;
    private Boolean smsStatus;
}