package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.UserInfoParam;

import java.util.List;

public interface IUserInfoBiz {
    SaberResponse<String> addUserInfo(UserInfoParam param);

    SaberResponse<String> addUserInfoByArray(String groupName, List<UserInfoParam> params);

    SaberResponse<String> updateUserInfo(int uid, UserInfoParam param);

    SaberResponse<PageData<UserInfo>> getUserInfoList(String dept, String keyword, int pageNo, int pageSize);

    SaberResponse<List<String>> getDepartmentList();
}