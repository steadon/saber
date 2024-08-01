package com.steadon.saber.handler.exception;

import com.steadon.saber.exception.SaberBaseException;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${notifications.larkBotEnabled:false}")
    private boolean larkBotEnabled;

    @ExceptionHandler(SaberBaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SaberResponse<Object>> handleSaberBaseException(SaberBaseException e) {
        log.error("SaberBaseException:\n{}", e.getMessage(), e);
        return new ResponseEntity<>(new SaberResponse<>(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SaberResponse<Object>> handleUncaughtException(Exception e) {
        if (larkBotEnabled) {
            String title = "线上BUG告警";
            String webhookUrl = "https://open.feishu.cn/open-apis/bot/v2/hook/11534a16-26e2-48f3-a425-9de2291c73ce";
            sendLarkNotification(webhookUrl, title, e.getMessage());
        }
        log.error("Uncaught Exception:\n{}", e.getMessage(), e);
        return new ResponseEntity<>(new SaberResponse<>(ResultCode.FAILURE, "internal error!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void sendLarkNotification(String webhookUrl, String title, String messageBody) {
        HttpClient client = HttpClient.newHttpClient();
        String json = String.format("""
                {
                    "msg_type": "post",
                    "content": {
                        "post": {
                            "zh_cn": {
                                "title": "%s",
                                "content": [
                                    [
                                        {
                                            "tag": "at",
                                            "user_id": "all"
                                        }
                                    ],
                                    [
                                        {
                                            "tag": "text",
                                            "text": "%s"
                                        }
                                    ]
                                ]
                            }
                        }
                    }
                }
                """, title, messageBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (Exception e) {
            throw new SaberBaseException("exception handle failed", e);
        }
    }
}
