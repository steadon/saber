package com.steadon.saber.handler.interceptor.auth;

import com.steadon.saber.common.AdminField;
import org.springframework.stereotype.Component;

@Component
public class SIPCInterceptor extends AuthInterceptor {

    @Override
    public boolean auth(String appId) {
        return !AdminField.SIPC.equals(appId) && !AdminField.ROOT.equals(appId);
    }
}
