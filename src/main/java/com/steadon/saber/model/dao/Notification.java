package com.steadon.saber.model.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.steadon.saber.biz.chain.model.MessageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@TableName("notification")
public class Notification implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("app_id")
    private String appId;

    @TableField("rule_id")
    private Integer ruleId;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("receiver")
    private String receiver;

    @TableField("strategy")
    private String strategy;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private String createTime;

    @TableField("trace_id")
    private String traceId;

    @TableField(exist = false)
    private Integer type;

    @TableField(exist = false)
    private String ruleCode;

    public Notification(MessageInfo messageInfo) {
        this.appId = messageInfo.getAppId();
        this.content = messageInfo.getContent();
    }
}
