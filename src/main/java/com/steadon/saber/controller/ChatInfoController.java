package com.steadon.saber.controller;

import com.steadon.saber.biz.IChatInfoBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.ChatInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ChatInfoParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 飞书群组管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class ChatInfoController {

    private final IChatInfoBiz iChatInfoBiz;

    /**
     * 分页获得群组信息
     */
    @GetMapping("/chat/list")
    public SaberResponse<PageData<ChatInfo>> getChatInfoList(@RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iChatInfoBiz.getChatInfoList(pageNo, pageSize);
    }

    /**
     * 新增群组
     */
    @PostMapping("/chat/add")
    public SaberResponse<String> addChatInfo(@RequestBody ChatInfoParam param) {
        return iChatInfoBiz.addChatInfo(param);
    }

    /**
     * 更新群组
     */
    @PostMapping("/chat/update")
    public SaberResponse<String> updateChatInfo(@RequestBody ChatInfoParam param) {
        return iChatInfoBiz.updateChatInfo(param);
    }

    /**
     * 删除群组
     */
    @PostMapping("/chat/delete")
    public SaberResponse<String> deleteChatInfo(@RequestParam("id") Integer chatId) {
        return iChatInfoBiz.deleteChatInfo(chatId);
    }
}
