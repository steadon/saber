package com.steadon.saber.handler.interceptor;

import com.steadon.saber.annotation.LogIgnored;
import com.steadon.saber.common.LogField;
import com.steadon.saber.util.TraceUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TraceIdInterceptor implements HandlerInterceptor, MessagePostProcessor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        MDC.put(LogField.TRACE_ID, String.valueOf(TraceUtils.getTraceId()));
        if (shouldLog(handler)) {
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);
            log.info("Request URL: {}, Method: {}, IP: {}", request.getRequestURL(), request.getMethod(), request.getRemoteAddr());
        }
        return true;
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        if (shouldLog(handler)) {
            long startTime = (Long) request.getAttribute("startTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            log.info("Status: {}, completed in {} ms", response.getStatus(), executeTime);
        }
        MDC.clear();
    }

    private boolean shouldLog(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return !(handlerMethod.getBeanType().isAnnotationPresent(LogIgnored.class) ||
                    handlerMethod.getMethod().isAnnotationPresent(LogIgnored.class));
        }
        return false;
    }

    /**
     * 消费者消费消息前的处理器
     *
     * @param message the message.
     * @return the message.
     * @throws AmqpException if an error occurs.
     */
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        MDC.put(LogField.TRACE_ID, String.valueOf(TraceUtils.getTraceId()));
        return message;
    }
}