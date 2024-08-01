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
@TableName("app_robot_merge")
public class AppRobotMerge implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("app_id")
    private String appId;

    @TableField("feishu_app_id")
    private String feishuAppId;

    @TableField("is_default")
    private Boolean isDefault;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
