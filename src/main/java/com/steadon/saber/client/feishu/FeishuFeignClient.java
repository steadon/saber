package com.steadon.saber.client.feishu;

import com.steadon.saber.client.feishu.model.FeishuNotificationRequest;
import com.steadon.saber.config.FeignRespConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "lancelot", configuration = FeignRespConfig.class)
public interface FeishuFeignClient {
    @PostMapping("/feishu/notification")
    int notification(@RequestBody FeishuNotificationRequest request);
}