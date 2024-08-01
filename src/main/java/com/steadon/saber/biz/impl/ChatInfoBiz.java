package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IChatInfoBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.ChatInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ChatInfoParam;
import com.steadon.saber.service.ChatInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatInfoBiz implements IChatInfoBiz {

    @Resource
    private ChatInfoService chatInfoService;

    /**
     * 获取群聊列表
     */
    @Override
    public SaberResponse<PageData<ChatInfo>> getChatInfoList(int pageNo, int pageSize) {
        PageData<ChatInfo> pageData = chatInfoService.queryList(pageNo, pageSize);
        return SaberResponse.success(pageData);
    }

    /**
     * 添加群聊
     */
    @Override
    public SaberResponse<String> addChatInfo(ChatInfoParam param) {
        String appId = TokenHandler.getAppId();
        String name = param.getName();
        String chatId = param.getChatId();
        String admin = TokenHandler.getUsername();

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setName(name);
        chatInfo.setChatId(chatId);
        chatInfo.setAppId(appId);
        chatInfo.setCreateBy(admin);
        chatInfoService.addOne(chatInfo);
        log.info("addedChatInfo: chatInfo={}", chatInfo);
        return SaberResponse.success();
    }

    /**
     * 更新群聊
     */
    @Override
    public SaberResponse<String> updateChatInfo(ChatInfoParam param) {
        int id = param.getId();
        String name = param.getName();
        String chatId = param.getChatId();

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setId(id);
        chatInfo.setName(name);
        chatInfo.setChatId(chatId);
        chatInfoService.updateOne(chatInfo);
        log.info("updatedChatInfo: chatInfo={}", chatInfo);
        return SaberResponse.success();
    }

    /**
     * 删除群聊
     */
    @Override
    public SaberResponse<String> deleteChatInfo(int id) {
        chatInfoService.deleteOne(id);
        log.info("deletedChatInfo: id={}", id);
        return SaberResponse.success();
    }
}
