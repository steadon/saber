package com.steadon.saber;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.steadon.saber.repository.mapper")
@EnableAsync
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class SaberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaberApplication.class, args);
    }

}