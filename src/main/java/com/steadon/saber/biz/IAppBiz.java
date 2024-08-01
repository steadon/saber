package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AccessParam;
import com.steadon.saber.model.param.AppParam;
import com.steadon.saber.model.param.BindRobotParam;
import com.steadon.saber.model.result.AccessResult;

public interface IAppBiz {

    SaberResponse<PageData<App>> getAppInfoList(int pageNo, int pageSize);

    SaberResponse<String> addAppInfo(AppParam param);

    SaberResponse<String> updateAppInfo(AppParam param);

    SaberResponse<String> deleteAppInfo(String appId);

    SaberResponse<String> bindRobot(BindRobotParam param);

    SaberResponse<App> updateAppSecret(String appId);

    SaberResponse<AccessResult> appAccess(AccessParam param);
}
