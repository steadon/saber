package com.steadon.saber.biz.chain.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Message {
    private final MessageStrategy strategy;
    private final MessageInfo messageInfo;
    private final Map<String, Object> params;
}