package com.steadon.saber.controller;

import com.steadon.saber.annotation.RateLimited;
import com.steadon.saber.biz.IAccessBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.param.AuthorizationParam;
import com.steadon.saber.model.result.TokenResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class AccessController {

    private final IAccessBiz iAccessBiz;

    /**
     * 登录
     */
    @RateLimited(0.2)
    @PostMapping("/login")
    public SaberResponse<TokenResult> authorization(@RequestBody AuthorizationParam param) {
        return iAccessBiz.authorization(param);
    }

    /**
     * 获得admin列表
     */
    @GetMapping("/admin/list")
    public SaberResponse<List<String>> getAdminList() {
        return iAccessBiz.getAdminList();
    }
}