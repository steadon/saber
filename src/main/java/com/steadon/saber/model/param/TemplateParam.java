package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class TemplateParam {
    private String name;
    private String description;
    private String content;
    private Boolean feishuStatus;
    private Boolean emailStatus;
    private Boolean smsStatus;
}
