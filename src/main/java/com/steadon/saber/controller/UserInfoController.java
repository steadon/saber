package com.steadon.saber.controller;

import com.steadon.saber.biz.IUserInfoBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.UserInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.UserInfoParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户模块
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class UserInfoController {

    private final IUserInfoBiz iUserInfoBiz;

    /**
     * 新增单个用户
     */
    @PostMapping("/user/add/single")
    public SaberResponse<String> addUserInfo(@RequestBody UserInfoParam param) {
        return iUserInfoBiz.addUserInfo(param);
    }

    /**
     * 批量新增用户
     */
    @PostMapping("/user/add")
    public SaberResponse<String> addUserInfoArray(@RequestParam(value = "g") String group,
                                                  @RequestBody List<UserInfoParam> params) {
        return iUserInfoBiz.addUserInfoByArray(group, params);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/user/update")
    public SaberResponse<String> updateUserInfo(@RequestParam(value = "uid") int uid,
                                                @RequestBody UserInfoParam param) {
        return iUserInfoBiz.updateUserInfo(uid, param);
    }

    /**
     * 分页获得用户信息
     */
    @GetMapping("/user/list")
    public SaberResponse<PageData<UserInfo>> getUserInfoList(@RequestParam(value = "d", required = false, defaultValue = "") String dept,
                                                             @RequestParam(value = "k", required = false, defaultValue = "") String keyword,
                                                             @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iUserInfoBiz.getUserInfoList(dept, keyword, pageNo, pageSize);
    }

    /**
     * 获得部门列表
     */
    @GetMapping("/user/department")
    public SaberResponse<List<String>> getDepartmentList() {
        return iUserInfoBiz.getDepartmentList();
    }
}
