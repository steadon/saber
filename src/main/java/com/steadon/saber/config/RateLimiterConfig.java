package com.steadon.saber.config;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Value("${rateLimiter.permitsPerSecond}")
    private double permitsPerSecond;

    @Value("${rateLimiter.timeoutInSeconds:1}")
    private long timeoutInSeconds;

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiter limiter = RateLimiter.create(permitsPerSecond);
        limiter.acquire((int) timeoutInSeconds);
        return limiter;
    }
}