package com.steadon.saber.service;

import com.steadon.saber.model.dao.ChatInfo;
import com.steadon.saber.model.page.PageData;

public interface ChatInfoService {

    PageData<ChatInfo> queryList(int pageNo, int pageSize);

    ChatInfo queryOne(String chatId);

    void addOne(ChatInfo chatInfo);

    void updateOne(ChatInfo chatInfo);

    void deleteOne(int id);
}
