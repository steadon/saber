package com.steadon.saber.biz.chain;

import com.steadon.saber.biz.chain.model.Message;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.client.feishu.FeishuFeignClient;
import com.steadon.saber.client.feishu.model.FeishuNotificationRequest;
import com.steadon.saber.enums.ExtraData;
import com.steadon.saber.model.dao.*;
import com.steadon.saber.service.*;
import com.steadon.saber.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.steadon.saber.common.LogField.FEIGN_TRACER;
import static com.steadon.saber.common.MsgType.*;
import static com.steadon.saber.common.NotificationType.FEISHU;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeishuChain extends BaseChain {

    private final FeishuRobotService feishuRobotService;
    private final NotificationAttrsService notificationAttrsService;
    private final UserGroupService userGroupService;
    private final ChatInfoService chatInfoService;
    private final TemplateService templateService;

    private final FeishuFeignClient feignClient;

    @Resource
    private Executor feishuChainExecutor;

    @Value("${lancelot.feishu-app-id}")
    private String LANCELOT_APP_ID;

    @Value("${lancelot.feishu-app-secret}")
    private String LANCELOT_APP_SECRET;

    @Override
    public String strategy() {
        return FEISHU;
    }

    @Override
    public Boolean shouldHandleMessage(Message message) {
        return message.getStrategy().feishuStatus();
    }

    @Override
    public Executor executor() {
        return feishuChainExecutor;
    }

    @Override
    public List<String> sendNotification(MessageInfo messageInfo, Map<String, Object> extraData) {

        Integer ruleId = messageInfo.getRuleId();
        NotificationFeishuAttrs feishuAttrs = notificationAttrsService.queryFeishuAttrs(ruleId);

        // 获取飞书通知需要的参数
        String appId = messageInfo.getAppId(); // 业务简写

        // 补全飞书消息推送通知维度
        String userId = StringUtils.EMPTY; // 配置用户（已删除）
        String chatId = feishuAttrs.getChatId(); // 配置群组
        String templateId = feishuAttrs.getTemplateId(); // 卡片ID
        String messageType = feishuAttrs.getMessageType();// 消息类型
        String feishuAppId = feishuAttrs.getFeishuAppId(); // 指定机器人

        // 指定参数覆盖配置参数
        if (extraData.containsKey(ExtraData.USER_ID.getFiled())) {
            userId = (String) extraData.get(ExtraData.USER_ID.getFiled());
        }
        if (extraData.containsKey(ExtraData.CHAT_ID.getFiled())) {
            chatId = (String) extraData.get(ExtraData.CHAT_ID.getFiled());
        }
        // 获取消息推送公共参数
        String title = (String) extraData.get(ExtraData.TITLE.getFiled());
        String content = messageInfo.getContent();
        List<UserInfo> userInfoList = messageInfo.getUserInfoList();

        // 合并单个通知用户于集合
        if (StringUtils.isNotEmpty(userId)) {
            UserInfo userInfo = userGroupService.queryUserInfo(userId);
            userInfoList.add(userInfo);
        }

        String finalFeishuAppId = LANCELOT_APP_ID;
        String finalFeishuAppSecret = LANCELOT_APP_SECRET;

        // TODO: 取消业务隔离，待后续对业务隔离做优化
        FeishuRobot feishuRobot = feishuRobotService.queryByFeishuAppId(feishuAppId);
        if (feishuRobot != null) {
            finalFeishuAppId = feishuRobot.getFeishuAppId();
            finalFeishuAppSecret = feishuRobot.getFeishuAppSecret();
        }

        // 整合飞书参数（不包括接收者）
        FeishuNotificationRequest feishuNotificationRequest = new FeishuNotificationRequest();
        feishuNotificationRequest.setFeishuAppId(finalFeishuAppId);
        feishuNotificationRequest.setFeishuAppSecret(finalFeishuAppSecret);
        feishuNotificationRequest.setTitle(title);
        feishuNotificationRequest.setChatId(chatId);
        feishuNotificationRequest.setContent(content);
        feishuNotificationRequest.setTemplateId(templateId);
        // 消息类型特殊处理
        if (POST.equals(messageType) && StringUtils.isEmpty(title)) {
            feishuNotificationRequest.setMessageType(TEXT);
        } else {
            feishuNotificationRequest.setMessageType(messageType);
        }
        // 消息卡片特殊处理
        if (CARD.equals(messageType) && extraData.containsKey(ExtraData.CARD_ATTRS.getFiled())) {
            feishuNotificationRequest.setTemplateArgs(extraData.get(ExtraData.CARD_ATTRS.getFiled()));
        }
        // 调用飞书下游服务发送消息
        for (UserInfo userInfo : userInfoList) {
            // TODO 处理预置参数（目前只支持用户信息相关）
            feishuNotificationRequest.setContent(templateService.handleTemplate(content, userInfo));
            // TODO 临时用抽象的方式处理
            feishuNotificationRequest.setUserIdList(List.of(userInfo.getUserId()));
            log.info("feishu feign request:{}", feishuNotificationRequest);
            int status = feignClient.notification(feishuNotificationRequest);
            log.info(FEIGN_TRACER + ":{}", status);
        }

        // 获取消息推送的接收者
        List<String> receivers = userInfoList.stream().map(UserInfo::getName).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(chatId)) {
            ChatInfo chatInfo = chatInfoService.queryOne(chatId);
            if (chatInfo != null) {
                receivers.add(chatInfo.getName());
            }
        }
        return receivers;
    }
}