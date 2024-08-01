package com.steadon.saber.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotificationRuleDto implements Serializable {
    private Integer id;
    private String code;
    private String description;
    private Integer templateId;
    private String templateName;
    private Integer groupId;
    private Integer type;
    private String groupName;
    private Boolean feishuStatus;
    private Boolean smsStatus;
    private Boolean emailStatus;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
}