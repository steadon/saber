package com.steadon.saber.handler.interceptor.auth;

import com.steadon.saber.common.AdminField;
import org.springframework.stereotype.Component;

@Component
public class ROOTInterceptor extends AuthInterceptor {

    @Override
    public boolean auth(String appId) {
        return !AdminField.ROOT.equals(appId);
    }
}
