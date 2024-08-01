package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class SearchNotificationRuleParam {
    private String keyword;
    private Integer type = 2;
    private String channel;
    private Integer groupId = 0;
    private String createBy;
}
