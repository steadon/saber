package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationRuleParam;
import com.steadon.saber.model.param.SearchNotificationRuleParam;

public interface INotificationRuleBiz {

    SaberResponse<NotificationRule> addNotificationRule(NotificationRuleParam param);

    SaberResponse<String> updateNotificationRule(int ruleId, NotificationRuleParam param);

    SaberResponse<String> deleteNotificationRule(int ruleId);

    SaberResponse<PageData<NotificationRuleDto>> getNotificationRuleList(SearchNotificationRuleParam param, int pageNo, int pageSize);
}
