package com.steadon.saber.biz.chain.model;

import com.steadon.saber.biz.chain.BaseChain;

public class MessageChain {

    private final BaseChain firstHandler;

    public MessageChain(BaseChain firstHandler) {
        this.firstHandler = firstHandler;
    }

    public void handleMessage(Message message) {
        firstHandler.handleMessage(message);
    }
}
