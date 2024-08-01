package com.steadon.saber.handler.interceptor.auth;

import com.steadon.saber.handler.token.TokenHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        if (auth(TokenHandler.getAppId())) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"result_code\":\"no_permission\",\"message\":\"没有权限访问\"}");
            return false;
        }
        return true;
    }

    public abstract boolean auth(String appId);
}
