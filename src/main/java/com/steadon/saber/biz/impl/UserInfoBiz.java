package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IUserInfoBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.UserInfoParam;
import com.steadon.saber.service.UserGroupService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserInfoBiz implements IUserInfoBiz {

    @Resource
    private UserGroupService userGroupService;

    /**
     * 新增单个用户
     */
    @Override
    public SaberResponse<String> addUserInfo(UserInfoParam param) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(param, userInfo);
        userInfo.setAppId(TokenHandler.getAppId());
        userInfo.setCreateBy(TokenHandler.getUsername());
        userGroupService.addOne(userInfo);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<String> addUserInfoByArray(String groupName, List<UserInfoParam> params) {
        userGroupService.bindToGroup(params, groupName);
        log.info("addUserInfoByArray: groupName={}, params={}", groupName, params);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<String> updateUserInfo(int uid, UserInfoParam param) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(uid);
        BeanUtils.copyProperties(param, userInfo);
        userGroupService.updateUserInfo(userInfo);
        log.info("updatedUserInfo: userInfo={}", userInfo);
        return SaberResponse.success();
    }

    @Override
    public SaberResponse<PageData<UserInfo>> getUserInfoList(String dept, String keyword, int pageNo, int pageSize) {
        PageData<UserInfo> userInfoPageData = userGroupService.queryUserList(dept, keyword, pageNo, pageSize);
        return SaberResponse.success(userInfoPageData);
    }

    @Override
    public SaberResponse<List<String>> getDepartmentList() {
        List<String> list = userGroupService.getDepartmentList();
        return SaberResponse.success(list);
    }
}