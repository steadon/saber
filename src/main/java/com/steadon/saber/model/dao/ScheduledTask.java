package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("scheduled_task")
@ToString
public class ScheduledTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // mybatis 的typeHandler默认只解析LocalDateTime到timestamp, 用long要自定义typeHandler
    @TableField("task_time")
    private LocalDateTime taskTime;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    // 不重复：0；每小时：1；每天：2；每周：3；每15天：4；每月：5
    @TableField("task_type")
    private Integer taskType;

    @TableField("app_id")
    private String appId;

    @TableField("rule_id")
    private Integer ruleId;

    @TableField("attrs")
    private String attrs;

    @TableField("uuid")
    private String uuid;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
