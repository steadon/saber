package com.steadon.saber.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {
    @Resource
    private ThreadPoolConfig threadPoolConfig;

    private Executor createExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(prefix);
        executor.initialize();
        return executor;
    }

    @Bean(name = "smsChainExecutor")
    public Executor smsChainExecutor() {
        return createExecutor(threadPoolConfig.getSms().getCorePoolSize(),
                threadPoolConfig.getSms().getMaxPoolSize(),
                threadPoolConfig.getSms().getQueueCapacity(),
                "sms-chain-async-");
    }

    @Bean(name = "feishuChainExecutor")
    public Executor feishuChainExecutor() {
        return createExecutor(threadPoolConfig.getFeishu().getCorePoolSize(),
                threadPoolConfig.getFeishu().getMaxPoolSize(),
                threadPoolConfig.getFeishu().getQueueCapacity(),
                "feishu-chain-async-");
    }

    @Bean(name = "emailChainExecutor")
    public Executor emailChainExecutor() {
        return createExecutor(threadPoolConfig.getEmail().getCorePoolSize(),
                threadPoolConfig.getEmail().getMaxPoolSize(),
                threadPoolConfig.getEmail().getQueueCapacity(),
                "email-chain-async-");
    }
}