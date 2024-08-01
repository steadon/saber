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
@TableName("admin")
public class Admin implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("phone")
    private String phone;

    @TableField("username")
    private String username;

    @JsonIgnore
    @TableField("password")
    private String password;

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
