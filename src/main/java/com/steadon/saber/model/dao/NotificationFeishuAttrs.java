package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author steadon
 * @since 2023-10-19
 */
@Data
@TableName("notification_feishu_attrs")
public class NotificationFeishuAttrs implements Serializable {

    @TableId(value = "rule_id")
    private Integer ruleId;

    @TableField("chat_id")
    private String chatId;

    @TableField("template_id")
    private String templateId;

    @TableField("feishu_app_id")
    private String feishuAppId;

    @TableField("message_type")
    private String messageType;

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
