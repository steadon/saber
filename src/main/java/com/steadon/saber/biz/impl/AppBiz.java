package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IAppBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AccessParam;
import com.steadon.saber.model.param.AppParam;
import com.steadon.saber.model.param.BindRobotParam;
import com.steadon.saber.model.result.AccessResult;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.AppService;
import com.steadon.saber.service.FeishuRobotService;
import com.steadon.saber.util.AccessTokenUtils;
import com.steadon.saber.util.RedisUtils;
import com.steadon.saber.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class AppBiz implements IAppBiz {

    private final AppService appService;
    private final FeishuRobotService feishuRobotService;

    /**
     * 获取上游业务列表
     */
    @Override
    public SaberResponse<PageData<App>> getAppInfoList(int pageNo, int pageSize) {
        PageData<App> pageData = appService.queryList(pageNo, pageSize);
        return SaberResponse.success(pageData);
    }

    /**
     * 添加上游业务
     */
    @Override
    public SaberResponse<String> addAppInfo(AppParam param) {
        if (appService.validAppId(param.getAppId()) != null) {
            return SaberResponse.appHasExist();
        }
        String appId = param.getAppId();
        String description = param.getDescription();
        String admin = TokenHandler.getUsername();

        App app = new App();
        app.setAppId(appId);
        app.setDescription(description);
        app.setAppSecret(AccessTokenUtils.getAppSecret(appId));
        app.setCreateBy(admin);
        appService.addOne(app);
        log.info("addedBizInfo: app={}", app);
        return SaberResponse.success();
    }

    /**
     * 更新上游业务
     */
    @Override
    public SaberResponse<String> updateAppInfo(AppParam param) {
        Integer id = appService.validAppId(param.getAppId());
        if (id == null) {
            return SaberResponse.appNotExist();
        }
        String appId = param.getAppId();
        String description = param.getDescription();

        App app = new App();
        app.setId(id);
        app.setAppId(appId);
        app.setDescription(description);
        appService.updateOne(app);
        log.info("updatedBizInfo: app={}", app);
        return SaberResponse.success();
    }

    /**
     * 删除上游业务
     */
    @Override
    public SaberResponse<String> deleteAppInfo(String appId) {
        Integer id = appService.validAppId(appId);
        if (id == null) {
            return SaberResponse.appNotExist();
        }
        appService.deleteOne(id);
        log.info("deletedBizInfo: appId={}", appId);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<String> bindRobot(BindRobotParam param) {
        // 查询业务方是否存在
        if (appService.validAppId(param.getAppId()) == null) {
            return SaberResponse.appNotExist();
        }
        // 查询机器人是否存在
        if (feishuRobotService.querySecret(param.getFeishuAppId()) == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "机器人不存在");
        }
        // 查询业务方是否已绑定机器人
        if (appService.queryMerge(param.getAppId(), param.getFeishuAppId())) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "机器人已绑定");
        }
        // 查询是否存在默认机器人
        if (param.getIsDefault() && appService.existDefaultRobot(param.getAppId())) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "默认机器人已存在");
        }
        // 绑定业务方与机器人
        appService.bindRobot(param.getAppId(), param.getFeishuAppId(), param.getIsDefault());
        log.info("boundRobot:appId={},feishuAppId={}", param.getAppId(), param.getFeishuAppId());
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<App> updateAppSecret(String appId) {
        App app = appService.updateAppSecret(appId);
        return SaberResponse.success(app);
    }

    @Override
    public SaberResponse<AccessResult> appAccess(AccessParam param) {
        // 查询业务方是否存在
        App app = appService.queryApp(param.getAppId(), param.getAppSecret());
        if (Objects.nonNull(app)) {
            if (StringUtils.isNotEmpty(app.getAccessToken())) {
                RedisUtils.deleteKey(app.getAccessToken());
            }
            String accessToken = AccessTokenUtils.getAccessToken(param.getAppId());
            app.setAccessToken(accessToken);
            appService.updateOne(app);

            AccessResult result = new AccessResult();
            result.setAccessToken(accessToken);
            return SaberResponse.success(result);
        }
        return SaberResponse.appNotExist();
    }
}
