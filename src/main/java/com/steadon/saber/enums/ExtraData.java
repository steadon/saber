package com.steadon.saber.enums;

import lombok.Getter;

@Getter
public enum ExtraData {
    // 公共
    TITLE("title"),
    // 飞书
    USER_ID("user_id"),
    CHAT_ID("chat_id"),
    CARD_ATTRS("card_attrs");

    private final String filed;

    ExtraData(String filed) {
        this.filed = filed;
    }
}
