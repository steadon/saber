package com.steadon.saber.service;

import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.dto.AppDto;
import com.steadon.saber.model.page.PageData;

import java.util.List;

public interface AppService {

    PageData<App> queryList(Integer pageNo, Integer pageSize);

    void addOne(App app);

    void updateOne(App app);

    void deleteOne(Integer appDaoId);

    Integer validAppId(String appId);

    List<AppDto> queryAppList(String feishuAppId);

    void bindRobot(String appId, String feishuAppId, Boolean isDefault);

    boolean existDefaultRobot(String appId);

    boolean queryMerge(String appId, String feishuAppId);

    App updateAppSecret(String appId);

    App queryApp(String appId, String appSecret);
}
