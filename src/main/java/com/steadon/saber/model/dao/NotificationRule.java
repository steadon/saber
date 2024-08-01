package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author steadon
 * @since 2023-10-08
 */

@Data
@TableName("notification_rule")
public class NotificationRule implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonIgnore
    @TableField("app_id")
    private String appId;

    @TableField("code")
    private String code;

    @TableField("description")
    private String description;

    @TableField("uid")
    private Integer uid;

    @TableField("group_id")
    private Integer groupId;

    @TableField("type")
    private Integer type;

    @TableField("template_id")
    private Integer templateId;

    @TableField("feishu_status")
    private Boolean feishuStatus;

    @TableField("email_status")
    private Boolean emailStatus;

    @TableField("sms_status")
    private Boolean smsStatus;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private String createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private String updateTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
