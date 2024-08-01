package com.steadon.saber.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledTaskDto {
    private Integer id;
    private String ruleCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime taskTime;
    private Integer taskType;
    private Boolean feishu;
    private Boolean sms;
    private Boolean email;
    private Integer groupId;
    private String createBy;
    private LocalDateTime createTime;
}