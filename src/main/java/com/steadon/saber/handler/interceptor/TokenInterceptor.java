package com.steadon.saber.handler.interceptor;

import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.TokenModel;
import com.steadon.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@CrossOrigin
@AllArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 从请求头中获取 JWT Token
        String token = request.getHeader("Authorization");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 验证token
        if (!tokenUtils.checkToken(token)) {
            // 设置 HTTP 状态码为 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 将载荷存入 ThreadLocal
        TokenModel tokenModel = tokenUtils.parseToken(token, TokenModel.class);
        TokenHandler.setTokenModel(tokenModel);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // 清除 ThreadLocal 中的数据
        TokenHandler.remove();
    }
}
