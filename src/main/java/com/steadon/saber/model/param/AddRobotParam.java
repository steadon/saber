package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class AddRobotParam {
    private String feishuAppId;
    private String feishuAppSecret;
    private String name;
    private String description;
}
