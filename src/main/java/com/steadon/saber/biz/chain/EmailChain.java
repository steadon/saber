package com.steadon.saber.biz.chain;

import com.steadon.saber.biz.chain.model.Message;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.client.email.EmailFeignClient;
import com.steadon.saber.client.email.model.EmailNotificationRequest;
import com.steadon.saber.enums.ExtraData;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.steadon.saber.common.LogField.FEIGN_TRACER;
import static com.steadon.saber.common.NotificationType.EMAIL;

@Slf4j
@Component
public class EmailChain extends BaseChain {

    @Resource
    private EmailFeignClient emailClient;

    @Resource
    private Executor emailChainExecutor;

    @Resource
    private TemplateService templateService;

    @Override
    public String strategy() {
        return EMAIL;
    }

    @Override
    public Boolean shouldHandleMessage(Message message) {
        return message.getStrategy().emailStatus();
    }

    @Override
    public Executor executor() {
        return emailChainExecutor;
    }

    @Override
    public List<String> sendNotification(MessageInfo messageInfo, Map<String, Object> extraData) {
        // 提取消息主体参数
        String title = (String) extraData.get(ExtraData.TITLE.getFiled());
        String content = messageInfo.getContent();

        // 提取用户邮箱集合
        for (UserInfo userInfo : messageInfo.getUserInfoList()) {
            if (userInfo.getEmail() == null) {
                continue;
            }
            String[] email = new String[]{userInfo.getEmail()};

            // 填充数据
            EmailNotificationRequest request = new EmailNotificationRequest();
            request.setSubject(title);
            request.setContent(templateService.handleTemplate(content, userInfo));
            request.setReceivers(email);

            // 发送邮件
            log.info("email feign request:{}", request);
            int status = emailClient.notification(request);
            log.info(FEIGN_TRACER + ":{}", status);
        }
        // 获取消息推送的接收者
        return messageInfo.getUserInfoList().stream().map(UserInfo::getName).toList();
    }
}
