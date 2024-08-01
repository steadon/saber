package com.steadon.saber;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class RabbitMQTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void sendMessageHappyPath() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "TEST");
        HashMap<String, String> params = new HashMap<>();
        params.put("content", "测试");
        map.put("params", params);
        HashMap<String, Object> extraData = new HashMap<>();
        extraData.put("user_id", "a48a5948");
        map.put("extra_data", extraData);
        map.put("access_token", "26b5c45a3384ae2f4647f3158112c878768970e1");
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend("saber.direct", "saber.message", map));
    }
}
