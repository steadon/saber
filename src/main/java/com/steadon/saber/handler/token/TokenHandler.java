package com.steadon.saber.handler.token;

import com.steadon.saber.model.TokenModel;

import java.util.Objects;

public class TokenHandler {

    private static final ThreadLocal<TokenModel> tokenModelThreadLocal = new ThreadLocal<>();

    public static TokenModel getTokenModel() {
        return tokenModelThreadLocal.get();
    }

    public static void setTokenModel(TokenModel model) {
        tokenModelThreadLocal.set(model);
    }

    public static String getUsername() {
        return tokenModelThreadLocal.get().getUsername();
    }

    public static String getAppId() {
        TokenModel tokenModel = tokenModelThreadLocal.get();
        if (Objects.isNull(tokenModel)) {
            return null;
        }
        return tokenModel.getAppId();
    }

    public static void remove() {
        tokenModelThreadLocal.remove();
    }
}