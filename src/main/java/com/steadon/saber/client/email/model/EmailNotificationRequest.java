package com.steadon.saber.client.email.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailNotificationRequest implements Serializable {
    // 收件人地址
    private String[] receivers;
    // 主题
    private String subject;
    // 内容
    private String content;
}
