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
@TableName("group_user_merge")
public class GroupUserMerge implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("uid")
    private Integer uid;

    @TableField("group_id")
    private Integer groupId;

    @JsonIgnore
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
