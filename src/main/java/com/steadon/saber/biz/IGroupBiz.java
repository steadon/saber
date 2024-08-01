package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.GroupParam;

public interface IGroupBiz {

    SaberResponse<PageData<Group>> getGroupList(String keyword, int pageNo, int pageSize);

    SaberResponse<String> updateGroup(int groupId, GroupParam param);

    SaberResponse<String> deleteGroup(int groupId);

    SaberResponse<Group> addGroup(GroupParam param);

    SaberResponse<Group> getGroupById(Integer groupId);
}
