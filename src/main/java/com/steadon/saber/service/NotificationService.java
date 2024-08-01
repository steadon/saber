package com.steadon.saber.service;

import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.model.dao.Notification;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchNotificationRuleParam;

import java.util.List;

public interface NotificationService {

    Integer addNotification(MessageInfo messageInfo, String strategy, String receiver);

    PageData<Notification> queryList(int pageNo, int pageSize);

    NotificationRule queryRuleById(int ruleId);

    NotificationRule queryRuleByCode(String ruleCode);

    Boolean checkRuleCode(String ruleCode);

    PageData<NotificationRuleDto> queryRuleList(SearchNotificationRuleParam param, int pageNo, int pageSize);

    void addRule(NotificationRule notificationRule);

    void updateRule(NotificationRule notificationRule);

    void deleteRule(int ruleId);

    NotificationRule verifyRuleCode(String ruleCode);

    void checkMsgStatus(int nid, String tid);

    List<NotificationRule> queryRulesByIds(List<Integer> idList);
}