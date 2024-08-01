package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author steadon
 * @since 2023-09-19
 */
@Data
@TableName("template")
public class Template implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("content")
    private String content;

    @TableField("feishu_status")
    private Boolean feishuStatus;

    @TableField("email_status")
    private Boolean emailStatus;

    @TableField("sms_status")
    private Boolean smsStatus;

    @JsonIgnore
    @TableField("app_id")
    private String appId;

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
