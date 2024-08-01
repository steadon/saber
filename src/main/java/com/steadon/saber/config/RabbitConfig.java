package com.steadon.saber.config;

import com.steadon.saber.handler.interceptor.TraceIdInterceptor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory);
        listenerContainerFactory.setMessageConverter(jackson2JsonMessageConverter());
        listenerContainerFactory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(4)
                .backOffOptions(1000, 2.0, 10000) // 初始间隔、乘数、最大间隔
                .build());
        // 设置消费者收到消息后的处理器
        listenerContainerFactory.setAfterReceivePostProcessors(beforePostProcessor());
        return listenerContainerFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory stringContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new SimpleMessageConverter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue messageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dlx.saber.direct");
        args.put("x-dead-letter-routing-key", "dlx.saber.message");
        return new Queue("message.queue", true, false, false, args);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("dlq.message.queue", true);
    }

    @Bean
    public Queue logQueue() {
        return new Queue("log.queue");
    }

    @Bean
    public DirectExchange saberExchange() {
        return new DirectExchange("saber.direct");
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx.saber.direct");
    }

    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange("log.direct");
    }

    @Bean
    public Binding binding(Queue messageQueue, DirectExchange saberExchange) {
        return BindingBuilder.bind(messageQueue).to(saberExchange).with("saber.message");
    }

    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with("saber.log");
    }

    @Bean
    public Binding DLQbinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dlx.saber.message");
    }

    @Bean
    public MessagePostProcessor beforePostProcessor() {
        return new TraceIdInterceptor();
    }
}
