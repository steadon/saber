package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.annotation.AppIsolated;
import com.steadon.saber.common.AdminField;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.exception.SaberInvalidException;
import com.steadon.saber.handler.helper.AuthHelper;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.base.HashAppId;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.dao.GroupUserMerge;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.dto.UserInfoDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.UserInfoParam;
import com.steadon.saber.repository.mapper.GroupMapper;
import com.steadon.saber.repository.mapper.GroupUserMergeMapper;
import com.steadon.saber.repository.mapper.UserInfoMapper;
import com.steadon.saber.service.UserGroupService;
import com.steadon.saber.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final GroupMapper groupMapper;
    private final UserInfoMapper userInfoMapper;
    private final GroupUserMergeMapper groupUserMergeMapper;

    @Override
    public void addOne(UserInfo userInfo) {
        if (userInfoMapper.insert(userInfo) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    @AppIsolated
    public List<UserInfo> queryUserInfo(Integer groupId) {
        if (groupId == null) {
            return Collections.emptyList();
        }
        QueryWrapper<Group> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        wrapper.eq("id", groupId);
        AuthHelper.checkPermission(groupMapper.selectOne(wrapper).getAppId());

        List<Integer> uidList = new ArrayList<>();
        QueryWrapper<GroupUserMerge> mergeWrapper = new QueryWrapper<GroupUserMerge>().eq("group_id", groupId);
        List<GroupUserMerge> mergeList = groupUserMergeMapper.selectList(mergeWrapper);
        for (GroupUserMerge merge : mergeList) {
            uidList.add(merge.getUid());
        }
        return uidList.isEmpty() ? null : userInfoMapper.selectBatchIds(uidList);
    }

    @Override
    @AppIsolated
    public PageData<UserInfo> queryUserList(String dept, String keyword, int pageNo, int pageSize) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        if (StringUtils.isNotEmpty(dept)) {
            wrapper.eq("department", dept);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.and(w -> w.like("name", keyword).or().like("email", keyword).or().like("mobile", keyword).or().like("user_id", keyword));
        }
        Page<UserInfo> page = new Page<>(pageNo, pageSize);
        Page<UserInfo> pageData = userInfoMapper.selectPage(page, wrapper);
        return new PageData<UserInfo>().praise(pageData);
    }

    @Override
    @AppIsolated
    public PageData<Group> queryGroupList(String keyword, int pageNo, int pageSize) {
        QueryWrapper<Group> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like("name", keyword);
        }
        Page<Group> page = new Page<>(pageNo, pageSize);
        wrapper.orderByDesc("create_time");
        Page<Group> pageData = groupMapper.selectPage(page, wrapper);
        return new PageData<Group>().praise(pageData);
    }

    @Override
    public Group queryGroup(Integer groupId) {
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            return null;
        }
        List<UserInfoDto> userInfoList = groupMapper.queryUserInfoById(groupId);
        group.setUserInfoList(userInfoList);
        return group;
    }

    @Override
    public void insertGroup(Group group) {
        throwIfGroupExist(group.getName());
        if (groupMapper.insert(group) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateGroup(Group group) {
        QueryWrapper<Group> wrapper = new QueryWrapper<>();
        wrapper.eq("id", group.getId());
        if (groupMapper.selectOne(wrapper) == null) {
            throw new SaberInvalidException("用户组不存在");
        }
        throwIfGroupExist(group.getName());
        if (groupMapper.updateById(group) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteGroup(Group group) {
        QueryWrapper<Group> wrapper = new QueryWrapper<>();
        if (groupMapper.selectOne(wrapper.eq("id", group.getId())) == null) {
            throw new SaberInvalidException("用户组不存在");
        }
        // 删除待删除用户组与用户的对应关系
        QueryWrapper<GroupUserMerge> newWrapper = new QueryWrapper<GroupUserMerge>().eq("group_id", group.getId());
        groupUserMergeMapper.delete(newWrapper);
        // 删除用户组
        if (groupMapper.deleteById(group) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public List<String> getDepartmentList() {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        List<UserInfo> groupList = userInfoMapper.selectList(wrapper);
        return groupList.stream().map(UserInfo::getDepartment).distinct().toList();
    }

    @Override
    public void bindToGroup(Integer groupId, List<Integer> userIdList) {
        for (Integer uid : userIdList) {
            // 检查用户是否已绑定到用户组,若未绑定则绑定
            QueryWrapper<GroupUserMerge> wrapper = new QueryWrapper<GroupUserMerge>().eq("id", groupId).eq("uid", uid);
            List<GroupUserMerge> mergeList = groupUserMergeMapper.selectList(wrapper);
            if (mergeList.isEmpty()) {
                GroupUserMerge merge = new GroupUserMerge();
                merge.setUid(uid);
                merge.setGroupId(groupId);
                if (groupUserMergeMapper.insert(merge) == 0) {
                    throw new SaberDBException();
                }
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void bindToGroup(List<UserInfoParam> requestList, String groupName) {
        String username = TokenHandler.getUsername();
        String appId = TokenHandler.getAppId();
        // 将请求参数转换为用户信息
        List<UserInfo> userInfoList = requestList.stream().map(request -> {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(request, userInfo);
            userInfo.setAppId(appId);
            userInfo.setCreateBy(username);
            return userInfo;
        }).toList();
        // 检查用户组是否存在，不存在则创建
        Group group = groupMapper.selectOne(new QueryWrapper<Group>().eq("name", groupName));
        if (group == null) {
            group = new Group();
            group.setName(groupName);
            group.setAppId(appId);
            group.setCreateBy(username);
            if (groupMapper.insert(group) == 0) {
                throw new SaberDBException();
            }
        }
        // 将用户信息绑定到用户组
        for (UserInfo userInfo : userInfoList) {
            // 检查用户是否存在，不存在则创建
            QueryWrapper<UserInfo> wrapper1 = new QueryWrapper<UserInfo>().eq("name", userInfo.getName()).eq("mobile", userInfo.getMobile()).eq("app_id", appId);
            List<UserInfo> infoList = userInfoMapper.selectList(wrapper1);
            if (infoList.isEmpty()) {
                if (userInfoMapper.insert(userInfo) == 0) {
                    throw new SaberDBException();
                }
            } else {
                userInfo = infoList.get(0);
            }
            // 检查用户是否已绑定到用户组,若未绑定则绑定
            QueryWrapper<GroupUserMerge> wrapper2 = new QueryWrapper<GroupUserMerge>().eq("id", group.getId()).eq("uid", userInfo.getId());
            List<GroupUserMerge> mergeList = groupUserMergeMapper.selectList(wrapper2);
            if (mergeList.isEmpty()) {
                GroupUserMerge merge = new GroupUserMerge();
                merge.setUid(userInfo.getId());
                merge.setGroupId(group.getId());
                if (groupUserMergeMapper.insert(merge) == 0) {
                    throw new SaberDBException();
                }
            }
        }
    }

    @Override
    public void clearBinding(Integer groupId) {
        QueryWrapper<GroupUserMerge> wrapper = new QueryWrapper<GroupUserMerge>().eq("group_id", groupId);
        if (groupUserMergeMapper.delete(wrapper) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userInfo.getId());
        if (userInfoMapper.update(userInfo, wrapper) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public UserInfo queryUserInfo(String userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        if (userInfo == null) {
            throw new SaberDBException();
        }
        return userInfo;
    }

    @Override
    public List<UserInfo> queryUserInfo(List<Integer> uidList) {
        return queryDataByIdList(uidList, userInfoMapper::selectBatchIds);
    }

    @Override
    public List<Group> queryGroupsByIdList(List<Integer> gidList) {
        return queryDataByIdList(gidList, groupMapper::selectBatchIds);
    }

    private <T extends HashAppId> List<T> queryDataByIdList(List<Integer> idList, Function<List<Integer>, List<T>> mapper) {
        if (CollectionUtils.isEmpty(idList)) {
            return List.of();
        }
        String appId = TokenHandler.getAppId();
        List<T> dataList = mapper.apply(idList);

        if (!AdminField.ROOT.equals(appId)) {
            dataList.removeIf(item -> !Objects.equals(appId, item.getAppId()));
        }
        return dataList;
    }

    private void throwIfGroupExist(String name) {
        QueryWrapper<Group> wrapper = new QueryWrapper<Group>()
                .eq("name", name)
                .eq("app_id", TokenHandler.getAppId());
        List<Group> list = groupMapper.selectList(wrapper);
        if (!list.isEmpty()) {
            throw new SaberInvalidException("用户组已存在");
        }
    }
}
