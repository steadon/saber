package com.steadon.saber.handler.helper;

import com.steadon.saber.common.AdminField;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.resultEnum.ResultCode;

public class AuthHelper {

    /**
     * 拦截非法用户脏读
     *
     * @param target 数据所属业务方
     */
    public static void checkPermission(String target) {
        String appId = TokenHandler.getAppId();
        if (!AdminField.ROOT.equals(appId) && !target.equals(appId)) {
            SaberResponse.failure(ResultCode.NO_PERMISSION, "没有权限更新该模版");
        }
    }
}
