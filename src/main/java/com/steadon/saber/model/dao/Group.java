package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.steadon.saber.model.base.HashAppId;
import com.steadon.saber.model.dto.UserInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author steadon
 * @since 2023-09-19
 */
@Data
@TableName("`group`")
public class Group implements Serializable, HashAppId {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @JsonIgnore
    @TableField("app_id")
    private String appId;

    @TableField("create_by")
    private String createBy;

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

    @TableField(exist = false)
    private List<UserInfoDto> userInfoList;
}
