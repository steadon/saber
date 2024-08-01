package com.steadon.saber.service;

import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.UserInfoParam;

import java.util.List;

public interface UserGroupService {

    void addOne(UserInfo userInfo);

    List<UserInfo> queryUserInfo(Integer groupId);

    List<UserInfo> queryUserInfo(List<Integer> uidList);

    UserInfo queryUserInfo(String userId);

    void bindToGroup(Integer groupId, List<Integer> userIdList);

    void bindToGroup(List<UserInfoParam> userInfoParams, String groupName);

    void clearBinding(Integer groupId);

    void updateUserInfo(UserInfo userInfo);

    PageData<UserInfo> queryUserList(String dept, String keyword, int pageNo, int pageSize);

    PageData<Group> queryGroupList(String keyword, int pageNo, int pageSize);

    Group queryGroup(Integer groupId);

    void insertGroup(Group group);

    void updateGroup(Group group);

    void deleteGroup(Group group);

    List<String> getDepartmentList();

    List<Group> queryGroupsByIdList(List<Integer> idList);
}