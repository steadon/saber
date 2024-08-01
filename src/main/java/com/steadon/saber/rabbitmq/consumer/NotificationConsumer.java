package com.steadon.saber.rabbitmq.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.steadon.saber.biz.INotificationBiz;
import com.steadon.saber.exception.SaberBaseException;
import com.steadon.saber.model.param.NotificationParam;
import com.steadon.saber.util.AccessTokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationConsumer {

    private INotificationBiz iNotificationBiz;

    @RabbitListener(queues = "message.queue")
    public void receiveMessage(Message message) {
        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("RabbitMQ Received Message: {}", messageStr);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        try {
            JsonNode jsonNode = objectMapper.readTree(messageStr);
            NotificationParam param = objectMapper.convertValue(jsonNode, NotificationParam.class);
            if (checkAccessToken(param)) {
                throw new SaberBaseException("Invalid access token");
            }
            iNotificationBiz.notification(param);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }

    private boolean checkAccessToken(NotificationParam param) {
        return AccessTokenUtils.checkAccessToken(param.getAccessToken()).isEmpty();
    }
}
