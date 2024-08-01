package com.steadon.saber.controller;

import com.steadon.saber.annotation.RateLimited;
import com.steadon.saber.biz.INotificationBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Notification;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.NotificationParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 通知管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class NotificationController {

    private final INotificationBiz iNotificationBiz;

    /**
     * 通知
     */
    @RateLimited(2.0)
    @PostMapping("/notification")
    public SaberResponse<String> notification(@RequestBody NotificationParam param) {
        return iNotificationBiz.notification(param);
    }

    /**
     * 获得通知记录
     */
    @GetMapping("/notification/list")
    public SaberResponse<PageData<Notification>> getNotificationList(@RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                                     @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        log.info("pageNo: {}, pageSize: {}", pageNo, pageSize);
        return iNotificationBiz.getNotificationList(pageNo, pageSize);
    }
}
