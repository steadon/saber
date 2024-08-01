package com.steadon.saber.model.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduledTaskParam implements Serializable {
    private Integer id;
    private Long taskTime;
    private Long startTime;
    private Long endTime;
    // 不重复：0；每小时：1；每天：2；每周：3；每15天：4；每月：5
    private Integer taskType;
    private NotificationParam notification;
}
