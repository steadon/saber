package com.steadon.saber.biz.chain;

import com.steadon.saber.biz.chain.model.Message;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.TokenModel;
import com.steadon.saber.service.NotificationService;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.steadon.saber.common.LogField.TRACE_ID;

@Slf4j
public abstract class BaseChain {

    @Setter
    public BaseChain successor;

    @Resource
    private NotificationService notificationService;

    public final void handleMessage(Message message) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        TokenModel tokenModel = TokenHandler.getTokenModel();
        if (shouldHandleMessage(message)) {
            executor().execute(() -> {
                MDC.setContextMap(contextMap);
                TokenHandler.setTokenModel(tokenModel);
                try {
                    List<String> receivers = sendNotification(message.getMessageInfo(), message.getParams());
                    addNotification(message.getMessageInfo(), strategy(), receivers);
                } catch (Exception e) {
                    log.error("Error handling message: {}", e.getMessage(), e);
                } finally {
                    MDC.clear();
                    TokenHandler.remove();
                }
            });
        }
        if (successor != null) {
            successor.handleMessage(message);
        }
    }

    public final void addNotification(MessageInfo messageInfo, String strategy, List<String> receivers) {
        List<Integer> nidList = new ArrayList<>();
        for (String receiver : receivers) {
            try {
                int nid = notificationService.addNotification(messageInfo, strategy, receiver);
                nidList.add(nid);
            } catch (Exception e) {
                log.error("Error adding notification: {}", e.getMessage(), e);
            }
        }
        // TODO 对于每个nid，都去检查有关记录和有关日志状态，但日志状态还需要nid进行区分
        for (int nid : nidList) {
            try {
                notificationService.checkMsgStatus(nid, MDC.get(TRACE_ID));
            } catch (Exception e) {
                log.error("Error checking message status: {}", e.getMessage(), e);
            }
        }
    }

    abstract public String strategy();

    abstract public Boolean shouldHandleMessage(Message message);

    abstract public Executor executor();

    abstract public List<String> sendNotification(MessageInfo messageInfo, Map<String, Object> extraData);
}