package com.steadon.saber.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "task.executor")
public class ThreadPoolConfig {

    private ThreadPoolProperties sms = new ThreadPoolProperties();
    private ThreadPoolProperties feishu = new ThreadPoolProperties();
    private ThreadPoolProperties email = new ThreadPoolProperties();

    @Data
    protected static class ThreadPoolProperties {
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
    }
}