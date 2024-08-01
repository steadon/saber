package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IGroupBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.GroupParam;
import com.steadon.saber.service.UserGroupService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupBiz implements IGroupBiz {

    @Resource
    private UserGroupService userGroupService;

    @Override
    public SaberResponse<PageData<Group>> getGroupList(String keyword, int pageNo, int pageSize) {
        PageData<Group> pageData = userGroupService.queryGroupList(keyword, pageNo, pageSize);
        if (pageData != null && pageData.getResultList() != null) {
            List<Group> resultList = pageData.getResultList().stream()
                    .map(group -> getGroup(group.getId()))
                    .collect(Collectors.toList());
            pageData.setResultList(resultList);
        }
        return SaberResponse.success(pageData);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SaberResponse<String> updateGroup(int groupId, GroupParam param) {
        Group updatedGroup = buildGroupFromParam(groupId, param);
        userGroupService.updateGroup(updatedGroup);
        userGroupService.clearBinding(groupId);
        userGroupService.bindToGroup(groupId, param.getUidList());
        return SaberResponse.success();
    }

    private Group buildGroupFromParam(int groupId, GroupParam param) {
        Group group = new Group();
        group.setId(groupId);
        group.setName(param.getName());
        group.setDescription(param.getDescription());
        group.setUpdateBy(TokenHandler.getUsername());
        return group;
    }

    @Override
    public SaberResponse<String> deleteGroup(int groupId) {
        Group group = new Group();
        group.setId(groupId);
        group.setUpdateBy(TokenHandler.getUsername());
        userGroupService.deleteGroup(group);
        log.info("deletedGroup: group={}", group);
        return SaberResponse.success();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SaberResponse<Group> addGroup(GroupParam param) {
        Group newGroup = buildGroupFromParam(param);
        userGroupService.insertGroup(newGroup);
        userGroupService.bindToGroup(newGroup.getId(), param.getUidList());
        newGroup = getGroup(newGroup.getId());
        return SaberResponse.success(newGroup);
    }

    private Group buildGroupFromParam(GroupParam param) {
        Group group = new Group();
        group.setName(param.getName());
        group.setDescription(param.getDescription());
        group.setCreateBy(TokenHandler.getUsername());
        group.setUpdateBy(TokenHandler.getUsername());
        group.setAppId(TokenHandler.getAppId());
        return group;
    }

    @Override
    public SaberResponse<Group> getGroupById(Integer groupId) {
        Group group = getGroup(groupId);
        if (group == null) {
            return SaberResponse.failure("group not found");
        }
        return SaberResponse.success(group);
    }

    private Group getGroup(Integer groupId) {
        return userGroupService.queryGroup(groupId);
    }
}
