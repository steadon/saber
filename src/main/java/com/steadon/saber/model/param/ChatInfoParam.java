package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class ChatInfoParam {

    /**
     * 群组id
     */
    private int id;

    /**
     * 群组飞书ids
     */
    private String chatId;

    /**
     * 群组名称
     */
    private String name;

}
