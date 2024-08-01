package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.steadon.saber.model.dto.AppDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author steadon
 * @since 2023-09-19
 */
@Data
@TableName("feishu_robot")
public class FeishuRobot implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("feishu_app_id")
    private String feishuAppId;

    @TableField("feishu_app_secret")
    private String feishuAppSecret;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private String createTime;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    @TableField(exist = false)
    private List<AppDto> appList;

    @TableField(exist = false)
    private String appId;
}
