package com.steadon.saber.controller;

import com.steadon.saber.biz.IGroupBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.GroupParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户组管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class GroupController {

    private final IGroupBiz iGroupBiz;

    /**
     * 查询用户组
     */
    @GetMapping("/group")
    public SaberResponse<Group> getGroupById(@RequestParam(value = "id") int groupId) {
        log.info("getGroupById: id={}", groupId);
        return iGroupBiz.getGroupById(groupId);
    }

    /**
     * 分页获得用户组
     */
    @GetMapping("/group/list")
    public SaberResponse<PageData<Group>> getGroupList(@RequestParam(value = "k", required = false, defaultValue = "") String keyword,
                                                       @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iGroupBiz.getGroupList(keyword, pageNo, pageSize);
    }

    /**
     * 新增用户组
     */
    @PostMapping("/group/add")
    public SaberResponse<Group> addGroup(@RequestBody GroupParam param) {
        return iGroupBiz.addGroup(param);
    }

    /**
     * 更新用户组
     */
    @PostMapping("/group/update")
    public SaberResponse<String> updateGroup(@RequestParam(value = "id") int groupId,
                                             @RequestBody GroupParam param) {
        return iGroupBiz.updateGroup(groupId, param);
    }

    /**
     * 删除用户组
     */
    @PostMapping("/group/delete")
    public SaberResponse<String> deleteGroup(@RequestParam(value = "id") int groupId) {
        return iGroupBiz.deleteGroup(groupId);
    }
}
