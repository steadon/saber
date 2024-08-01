package com.steadon.saber.controller;

import com.steadon.saber.biz.IAppBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AccessParam;
import com.steadon.saber.model.param.AppParam;
import com.steadon.saber.model.param.BindRobotParam;
import com.steadon.saber.model.result.AccessResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 上游业务管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class AppController {

    private final IAppBiz IAppBiz;

    /**
     * 分页获取app
     */
    @GetMapping("/app/list")
    public SaberResponse<PageData<App>> getAppInfoList(@RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return IAppBiz.getAppInfoList(pageNo, pageSize);
    }

    /**
     * 新增app
     */
    @PostMapping("/app/add")
    public SaberResponse<String> addAppInfo(@RequestBody AppParam param) {
        return IAppBiz.addAppInfo(param);
    }

    /**
     * 更新app
     */
    @PostMapping("/app/update")
    public SaberResponse<String> updateAppInfo(@RequestBody AppParam request) {
        log.info("updateAppInfo: request={}", request);
        return IAppBiz.updateAppInfo(request);
    }

    /**
     * 删除app
     */
    @PostMapping("/app/delete")
    public SaberResponse<String> deleteAppInfo(@RequestParam(value = "app_id") String appId) {
        return IAppBiz.deleteAppInfo(appId);
    }

    /**
     * 绑定app和bot
     */
    @PostMapping("/app/bind")
    public SaberResponse<String> bindRobot(@RequestBody BindRobotParam param) {
        return IAppBiz.bindRobot(param);
    }

    /**
     * 更新appSecret
     */
    @PostMapping("/app/secret/update")
    public SaberResponse<App> updateAppSecret(@RequestParam(value = "app_id") String appId) {
        return IAppBiz.updateAppSecret(appId);
    }

    /**
     * 应用方授权
     */
    @PostMapping("/app/access")
    public SaberResponse<AccessResult> appAccess(@RequestBody AccessParam param) {
        return IAppBiz.appAccess(param);
    }
}
