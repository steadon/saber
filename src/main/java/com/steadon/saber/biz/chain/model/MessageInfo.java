package com.steadon.saber.biz.chain.model;

import com.steadon.saber.model.dao.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MessageInfo {

    private Integer ruleId;

    private String content; // 消息内容
    private List<UserInfo> userInfoList; // 推送对象

    private String appId;

    public MessageInfo(String appId, Integer ruleId) {
        this.ruleId = ruleId;
        this.appId = appId;
    }
}