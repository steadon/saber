package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("notification_sms_attrs")
public class NotificationSmsAttrs implements Serializable {

    @TableId(value = "rule_id")
    private Integer ruleId;

    @TableField("template_id")
    private String templateId;

    @TableField("sign_name")
    private String signName;

    @JsonIgnore
    @TableField("create_by")
    private String createBy;

    @JsonIgnore
    @TableField("create_time")
    private String createTime;

    @JsonIgnore
    @TableField("update_by")
    private String updateBy;

    @JsonIgnore
    @TableField("update_time")
    private String updateTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
