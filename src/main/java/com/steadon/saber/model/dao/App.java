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
@TableName("app")
public class App implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("app_id")
    private String appId;

    @TableField("description")
    private String description;

    @TableField("app_secret")
    private String appSecret;

    @TableField("access_token")
    private String accessToken;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private String createTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
