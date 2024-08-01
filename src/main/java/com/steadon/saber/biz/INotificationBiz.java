package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Notification;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationParam;

public interface INotificationBiz {

    SaberResponse<String> notification(NotificationParam param);

    SaberResponse<PageData<Notification>> getNotificationList(int pageNo, int pageSize);
}
