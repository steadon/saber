package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.param.FeishuAttrsParam;

public interface INotificationAttrsBiz {

    SaberResponse<String> updateFeishuAttrs(FeishuAttrsParam param);

    SaberResponse<NotificationFeishuAttrs> queryFeishuAttrs(Integer ruleId);
}