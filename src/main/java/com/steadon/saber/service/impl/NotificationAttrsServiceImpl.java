package com.steadon.saber.service.impl;

import com.steadon.saber.exception.SaberBaseException;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.handler.helper.AuthHelper;
import com.steadon.saber.model.dao.NotificationFeishuAttrs;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.NotificationSmsAttrs;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.repository.mapper.NotificationFeishuAttrsMapper;
import com.steadon.saber.repository.mapper.NotificationRuleMapper;
import com.steadon.saber.repository.mapper.NotificationSmsAttrsMapper;
import com.steadon.saber.service.NotificationAttrsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationAttrsServiceImpl implements NotificationAttrsService {

    private final NotificationFeishuAttrsMapper notificationFeishuAttrsMapper;
    private final NotificationSmsAttrsMapper notificationSmsAttrsMapper;
    private final NotificationRuleMapper notificationRuleMapper;

    @Override
    public NotificationFeishuAttrs queryFeishuAttrs(Integer ruleId) {
        NotificationRule notificationRule = notificationRuleMapper.selectById(ruleId);
        if (notificationRule == null) {
            throw new SaberBaseException(ResultCode.INVALID_PARAM, "规则不存在");
        }
        AuthHelper.checkPermission(notificationRule.getAppId());
        return notificationFeishuAttrsMapper.selectById(ruleId);
    }

    @Override
    public NotificationSmsAttrs querySmsAttrs(int ruleId) {
        AuthHelper.checkPermission(notificationRuleMapper.selectById(ruleId).getAppId());
        return notificationSmsAttrsMapper.selectById(ruleId);
    }

    @Override
    public void addSmsAttrs(NotificationSmsAttrs smsAttrs) {
        AuthHelper.checkPermission(notificationRuleMapper.selectById(smsAttrs.getRuleId()).getAppId());
        if (notificationSmsAttrsMapper.insert(smsAttrs) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deleteFeishuAttrs(int ruleId) {
        AuthHelper.checkPermission(notificationRuleMapper.selectById(ruleId).getAppId());
        if (notificationFeishuAttrsMapper.deleteById(ruleId) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deleteSmsAttrs(int ruleId) {
        AuthHelper.checkPermission(notificationRuleMapper.selectById(ruleId).getAppId());
        if (notificationSmsAttrsMapper.deleteById(ruleId) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void addFeishuAttrs(NotificationFeishuAttrs feishuAttrs) {
        AuthHelper.checkPermission(notificationRuleMapper.selectById(feishuAttrs.getRuleId()).getAppId());
        if (notificationFeishuAttrsMapper.insert(feishuAttrs) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateFeishuAttrs(NotificationFeishuAttrs feishuAttrs) {
        NotificationRule notificationRule = notificationRuleMapper.selectById(feishuAttrs.getRuleId());
        AuthHelper.checkPermission(notificationRule.getAppId());
        if (notificationFeishuAttrsMapper.updateById(feishuAttrs) == 0) {
            throw new SaberDBException();
        }
    }
}