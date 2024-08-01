package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.INotificationBiz;
import com.steadon.saber.biz.chain.model.Message;
import com.steadon.saber.biz.chain.model.MessageChain;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.biz.chain.model.MessageStrategy;
import com.steadon.saber.handler.factory.MessageStrategyFactory;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Notification;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationParam;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.NotificationService;
import com.steadon.saber.service.TemplateService;
import com.steadon.saber.service.UserGroupService;
import com.steadon.saber.util.AccessTokenUtils;
import com.steadon.saber.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationBiz implements INotificationBiz {

    private final TemplateService templateService;
    private final UserGroupService userGroupService;
    private final NotificationService notificationService;

    private final MessageChain messageChain;

    private final MessageStrategyFactory messageStrategyFactory;

    @Override
    public SaberResponse<String> notification(NotificationParam param) {

        String appId = getAppId(param);
        if (appId.isEmpty()) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "Token不存在");
        }
        Map<String, Object> attrsMap = param.getExtraData();

        // 查询通知规则
        NotificationRule notificationRule = notificationService.queryRuleByCode(param.getCode());
        if (notificationRule == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "通知规则不存在");
        }
        // 初步处理消息模版（不包含预置参数）
        String content = templateService.handleTemplate(notificationRule.getTemplateId(), param.getParams());

        // 查询并整合目标用户集合
        List<UserInfo> userInfoList = new ArrayList<>();
        List<UserInfo> usersFormParam = userGroupService.queryUserInfo(param.getUidList());
        List<UserInfo> usersFormGroup = userGroupService.queryUserInfo(notificationRule.getGroupId());
        userInfoList.addAll(usersFormParam);
        userInfoList.addAll(usersFormGroup);

        // 整合公共层参数集合
        MessageInfo messageInfo = new MessageInfo(appId, notificationRule.getId());

        // 整合公共通知参数集合
        messageInfo.setContent(content);
        messageInfo.setUserInfoList(userInfoList.stream().distinct().collect(Collectors.toList()));

        // 创建通知发送策略
        MessageStrategy strategy = messageStrategyFactory.createStrategy(notificationRule);

        // 责任链处理消息分发
        log.info("strategy: {}, messageInfo: {}, attrsMap: {}", strategy, messageInfo, attrsMap);
        messageChain.handleMessage(Message.builder().strategy(strategy).messageInfo(messageInfo).params(attrsMap).build());

        // 响应操作状态而非分布式事务状态
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<PageData<Notification>> getNotificationList(int pageNo, int pageSize) {
        PageData<Notification> pageData = notificationService.queryList(pageNo, pageSize);
        return SaberResponse.success(pageData);
    }

    /**
     * 从accessToken或者token中获取appId
     */
    private String getAppId(NotificationParam request) {
        String appId = AccessTokenUtils.checkAccessToken(request.getAccessToken())
                .orElseGet(TokenHandler::getAppId);
        return appId == null ? StringUtils.EMPTY : appId;
    }
}