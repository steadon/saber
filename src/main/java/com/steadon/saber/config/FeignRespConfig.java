package com.steadon.saber.config;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeignRespConfig {

    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder encoder() {
        return new SpringEncoder(messageConverters);
    }

    @Bean
    public Decoder decoder() {
        return (response, type) -> response.status();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
