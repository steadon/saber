package com.steadon.saber.client.sms;

import com.steadon.saber.client.sms.model.SmsNotificationRequest;
import com.steadon.saber.config.FeignRespConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sms", configuration = FeignRespConfig.class)
public interface SmsFeignClient {
    @PostMapping("/sms/notification")
    int notification(@RequestBody SmsNotificationRequest request);
}
