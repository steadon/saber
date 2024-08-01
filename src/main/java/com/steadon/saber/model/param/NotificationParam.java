package com.steadon.saber.model.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class NotificationParam implements Serializable {
    private String code; // 规则code

    private List<Integer> uidList; // 动态用户集合
    private Map<String, Object> extraData; // 动态参数集合
    private Map<String, String> params; // 模版参数集合

    private String accessToken; // 访问令牌
}