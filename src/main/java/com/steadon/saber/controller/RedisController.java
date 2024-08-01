package com.steadon.saber.controller;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.util.RedisUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis后门
 */
@RestController("/redis")
public class RedisController {
    /**
     * 删除所有缓存
     */
    @PostMapping("/clear")
    public SaberResponse<String> clear() {
        RedisUtils.deletedAllKey();
        return SaberResponse.success();
    }
}
