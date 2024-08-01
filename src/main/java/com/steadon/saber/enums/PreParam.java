package com.steadon.saber.enums;

import lombok.Getter;

@Getter
public enum PreParam {

    name("接收者姓名"),
    department("接收者部门"),
    email("接收者邮箱"),
    mobile("接收者电话"),
    userId("接收者飞书ID"),
    appId("接收者业务方ID");

    private final String desc;

    PreParam(String desc) {
        this.desc = desc;
    }
}
