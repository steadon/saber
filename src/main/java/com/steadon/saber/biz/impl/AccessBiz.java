package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IAccessBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.TokenModel;
import com.steadon.saber.model.dao.Admin;
import com.steadon.saber.model.param.AuthorizationParam;
import com.steadon.saber.model.result.TokenResult;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.AccessService;
import com.steadon.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccessBiz implements IAccessBiz {

    private final AccessService accessService;

    private final TokenUtils tokenUtils;

    /**
     * 登录授权，支持手机号和用户名登录
     */
    @Override
    public SaberResponse<TokenResult> authorization(AuthorizationParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        // 用户传参为空校验
        if (username.isEmpty()) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "用户传参为空");
        }
        // 查询用户是否存在
        Admin admin = accessService.queryAdmin(username);
        if (admin == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "账户不存在");
        }
        // 密码校验
        if (!admin.getPassword().equals(password)) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "密码错误");
        }
        // 生成TokenModel
        TokenModel tokenModel = new TokenModel();
        tokenModel.setAppId(admin.getAppId());
        tokenModel.setUsername(admin.getUsername());

        // 签发令牌并返回给前端
        TokenResult result = new TokenResult();
        result.setUsername(admin.getUsername());
        result.setToken(tokenUtils.createToken(tokenModel));
        return SaberResponse.success(result);
    }

    /**
     * 获取管理员列表
     */
    @Override
    public SaberResponse<List<String>> getAdminList() {
        List<String> adminList = accessService.queryAdminList();
        return SaberResponse.success(adminList);
    }
}
