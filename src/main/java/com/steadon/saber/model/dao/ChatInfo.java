package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author steadon
 * @since 2023-09-20
 */
@Data
@TableName("chat_info")
public class ChatInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("chat_id")
    private String chatId;

    @JsonIgnore
    @TableField("app_id")
    private String appId;

    @JsonIgnore
    @TableField("create_by")
    private String createBy;

    @JsonIgnore
    @TableField("create_time")
    private String createTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
