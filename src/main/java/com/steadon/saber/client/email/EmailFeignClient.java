package com.steadon.saber.client.email;

import com.steadon.saber.client.email.model.EmailNotificationRequest;
import com.steadon.saber.config.FeignRespConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mnemosyne", configuration = FeignRespConfig.class)
public interface EmailFeignClient {
    @PostMapping("/email/send")
    int notification(@RequestBody EmailNotificationRequest request);
}
