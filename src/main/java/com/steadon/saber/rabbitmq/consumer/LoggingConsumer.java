package com.steadon.saber.rabbitmq.consumer;

import com.steadon.saber.model.dao.Log;
import com.steadon.saber.repository.mongo.LogRepository;
import com.steadon.saber.util.LogUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingConsumer {

    @Resource
    private LogRepository logRepository;

    @RabbitListener(queues = "log.queue", containerFactory = "stringContainerFactory")
    public void receiveLog(String logStr) {
        Log log = LogUtils.praiseLogStr(logStr);
        if (log == null) {
            return;
        }
        logRepository.save(log);
    }
}
