package com.steadon.saber.aop;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.steadon.saber.annotation.RateLimited;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.steadon.saber.annotation.RateLimited)")
    public void rateLimitPointcut() {
    }

    @Around("rateLimitPointcut()")
    public Object applyRateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RateLimited rateLimited = method.getAnnotation(RateLimited.class);

        // 生成限流器的键：类名.方法名[参数类型]
        String key = generateKey(method);

        RateLimiter limiter = limiters.computeIfAbsent(key, k -> RateLimiter.create(rateLimited.value()));

        // 尝试获取许可
        if (limiter.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            // 创建限流提示信息
            log.warn("Rate limit exceeded");
            String rateLimitInfo = String.format("Rate limit exceeded. You can only make one request every %.2f seconds.", 1.0 / rateLimited.value());
            return SaberResponse.failure(ResultCode.RATE_LIMIT, rateLimitInfo);
        }
    }

    // 生成包含方法签名的键，以区分重载方法
    private String generateKey(Method method) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getDeclaringClass().getName())
                .append(".").append(method.getName())
                .append("(");
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i > 0) keyBuilder.append(",");
            keyBuilder.append(paramTypes[i].getTypeName());
        }
        keyBuilder.append(")");
        return keyBuilder.toString();
    }
}