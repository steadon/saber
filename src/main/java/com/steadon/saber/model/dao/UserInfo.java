package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.steadon.saber.model.base.HashAppId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author steadon
 * @since 2023-09-20
 */
@Data
@TableName("user_info")
public class UserInfo implements Serializable, HashAppId {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("department")
    private String department;

    @TableField("email")
    private String email;

    @TableField("mobile")
    private String mobile;

    @TableField("user_id")
    private String userId;

    @JsonIgnore
    @TableField("app_id")
    private String appId;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private String createTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
