package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class SearchTemplateParam {
    private String keyword;
    private Boolean feishuStatus = false;
    private Boolean emailStatus = false;
    private Boolean smsStatus = false;
    private String createBy;
}
