package com.steadon.saber.model;

import com.steadon.annotation.Token;
import lombok.Data;

@Data
public class TokenModel {
    @Token
    private String appId;
    @Token
    private String username;
}
