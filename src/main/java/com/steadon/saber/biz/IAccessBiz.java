package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.param.AuthorizationParam;
import com.steadon.saber.model.result.TokenResult;

import java.util.List;

public interface IAccessBiz {

    SaberResponse<TokenResult> authorization(AuthorizationParam param);

    SaberResponse<List<String>> getAdminList();
}