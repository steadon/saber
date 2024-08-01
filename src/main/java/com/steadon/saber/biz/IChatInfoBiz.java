package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.ChatInfo;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ChatInfoParam;

public interface IChatInfoBiz {

    SaberResponse<PageData<ChatInfo>> getChatInfoList(int pageNo, int pageSize);

    SaberResponse<String> addChatInfo(ChatInfoParam param);

    SaberResponse<String> updateChatInfo(ChatInfoParam param);

    SaberResponse<String> deleteChatInfo(int chatId);
}
