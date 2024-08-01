package com.steadon.saber.client.sms.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsNotificationRequest implements Serializable {
    //消息模版
    private String signName;

    private String phone;
    private String content;
}
