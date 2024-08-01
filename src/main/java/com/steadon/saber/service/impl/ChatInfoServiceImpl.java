package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.annotation.AppIsolated;
import com.steadon.saber.common.AdminField;
import com.steadon.saber.exception.SaberBaseException;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.dao.ChatInfo;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.repository.mapper.ChatInfoMapper;
import com.steadon.saber.repository.mapper.NotificationFeishuAttrsMapper;
import com.steadon.saber.service.ChatInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatInfoServiceImpl implements ChatInfoService {

    private final ChatInfoMapper chatInfoMapper;
    private final NotificationFeishuAttrsMapper notificationFeishuAttrsMapper;

    @Override
    @AppIsolated
    public PageData<ChatInfo> queryList(int pageNo, int pageSize) {
        QueryWrapper<ChatInfo> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        Page<ChatInfo> page = new Page<>(pageNo, pageSize);
        Page<ChatInfo> pageData = chatInfoMapper.selectPage(page, wrapper);
        return new PageData<ChatInfo>().praise(pageData);
    }

    @Override
    @AppIsolated
    public ChatInfo queryOne(String chatId) {
        QueryWrapper<ChatInfo> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        wrapper.eq("chat_id", chatId);
        return chatInfoMapper.selectOne(wrapper);
    }

    @Override
    public void addOne(ChatInfo chatInfo) {
        if (chatInfoMapper.insert(chatInfo) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateOne(ChatInfo chatInfo) {
        QueryWrapper<ChatInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", chatInfo.getId());
        if (chatInfoMapper.update(chatInfo, wrapper) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deleteOne(int id) {
        QueryWrapper<ChatInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        ChatInfo chatInfo = chatInfoMapper.selectOne(wrapper);
        if (chatInfo != null) {
            //查找改群组是否被其他规则使用
            QueryWrapper<NotificationFeishuAttrs> wrapper2 = new QueryWrapper<NotificationFeishuAttrs>().eq("chat_id", chatInfo.getChatId()).last("limit 1");
            NotificationFeishuAttrs attrs = notificationFeishuAttrsMapper.selectOne(wrapper2);
            if (attrs != null) {
                throw new SaberBaseException(ResultCode.FAILURE, "群聊正被使用，无法删除");
            }
            if (chatInfoMapper.deleteById(id) == 0) {
                throw new SaberDBException();
            }
        }
    }
}
