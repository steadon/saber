package com.steadon.saber.biz.chain;

import com.steadon.saber.biz.chain.model.Message;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.client.sms.SmsFeignClient;
import com.steadon.saber.client.sms.model.SmsNotificationRequest;
import com.steadon.saber.exception.SaberInvalidException;
import com.steadon.saber.model.dao.NotificationSmsAttrs;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.service.NotificationAttrsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import static com.steadon.saber.common.LogField.FEIGN_TRACER;
import static com.steadon.saber.common.NotificationType.SMS;

@Slf4j
@Component
public class SmsChain extends BaseChain {

    @Resource
    private NotificationAttrsService notificationAttrsService;

    @Resource
    private SmsFeignClient smsFeignClient;

    @Resource
    private Executor smsChainExecutor;

    @Override
    public String strategy() {
        return SMS;
    }

    @Override
    public Boolean shouldHandleMessage(Message message) {
        return message.getStrategy().smsStatus();
    }

    @Override
    public Executor executor() {
        return smsChainExecutor;
    }

    @Override
    public List<String> sendNotification(MessageInfo messageInfo, Map<String, Object> extraData) {
        // 通过ruleId查询具体的短信发送信息
        NotificationSmsAttrs notificationSmsAttrs = notificationAttrsService.querySmsAttrs(messageInfo.getRuleId());

        // TODO: 群发实现-循环单发（网络IO过高需要修改）
        SmsNotificationRequest smsNotificationRequest = new SmsNotificationRequest();
        smsNotificationRequest.setContent(messageInfo.getContent());
        smsNotificationRequest.setSignName(notificationSmsAttrs.getSignName());

        String regex = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        Pattern pattern = Pattern.compile(regex);

        List<String> mobileList = messageInfo.getUserInfoList().stream().map(UserInfo::getMobile).toList();

        for (String mobile : mobileList) {
            if (pattern.matcher(mobile).matches()) {
                smsNotificationRequest.setPhone(mobile);
                log.info("sms request:{}", smsNotificationRequest);
                int status = smsFeignClient.notification(smsNotificationRequest);
                log.info(FEIGN_TRACER + ":{}", status);
            } else {
                throw new SaberInvalidException("手机号异常");
            }
        }
        // 获取消息推送的接收者
        return messageInfo.getUserInfoList().stream().map(UserInfo::getName).toList();
    }
}
