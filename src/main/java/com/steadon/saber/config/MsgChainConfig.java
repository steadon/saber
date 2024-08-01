package com.steadon.saber.config;

import com.steadon.saber.biz.chain.EmailChain;
import com.steadon.saber.biz.chain.FeishuChain;
import com.steadon.saber.biz.chain.SmsChain;
import com.steadon.saber.biz.chain.model.MessageChain;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chain of Responsibility
 *
 * @author steadon.wei
 */
@Configuration
@RequiredArgsConstructor
public class MsgChainConfig {

    private final SmsChain smsChain;
    private final EmailChain emailChain;
    private final FeishuChain feishuChain;

    @Bean
    public MessageChain messageHandlerChain() {
        emailChain.setSuccessor(smsChain);
        feishuChain.setSuccessor(emailChain);
        return new MessageChain(feishuChain);
    }
}
