package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.biz.chain.model.MessageInfo;
import com.steadon.saber.common.LogField;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.dao.Notification;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchNotificationRuleParam;
import com.steadon.saber.repository.mapper.NotificationMapper;
import com.steadon.saber.repository.mapper.NotificationRuleMapper;
import com.steadon.saber.repository.mongo.LogRepository;
import com.steadon.saber.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final NotificationRuleMapper notificationRuleMapper;
    private final LogRepository logRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Integer addNotification(MessageInfo messageInfo, String strategy, String receiver) {
        Notification notification = new Notification(messageInfo);
        notification.setRuleId(messageInfo.getRuleId());
        notification.setReceiver(receiver);
        notification.setStrategy(strategy);
        notification.setTraceId(MDC.get(LogField.TRACE_ID));
        notificationMapper.insert(notification);
        return notification.getId();
    }

    @Override
    public NotificationRule queryRuleByCode(String ruleCode) {
        return notificationRuleMapper.selectOne(new QueryWrapper<NotificationRule>().eq("code", ruleCode));
    }

    @Override
    public Boolean checkRuleCode(String ruleCode) {
        List<NotificationRule> list = notificationRuleMapper.selectList(new QueryWrapper<NotificationRule>().eq("code", ruleCode));
        return list.isEmpty();
    }

    @Override
    public NotificationRule queryRuleById(int ruleId) {
        return notificationRuleMapper.selectById(ruleId);
    }

    @Override
    public void addRule(NotificationRule notificationRule) {
        if (notificationRuleMapper.insert(notificationRule) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateRule(NotificationRule notificationRule) {
        if (notificationRuleMapper.updateById(notificationRule) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deleteRule(int ruleId) {
        if (notificationRuleMapper.deleteById(ruleId) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public PageData<NotificationRuleDto> queryRuleList(SearchNotificationRuleParam param, int pageNo, int pageSize) {
        String appId = TokenHandler.getAppId();
        Page<NotificationRule> page = new Page<>(pageNo, pageSize);
        IPage<NotificationRuleDto> pageData = notificationRuleMapper.selectVoPage(param, appId, page);
        return new PageData<NotificationRuleDto>().praise(pageData);
    }

    @Override
    public PageData<Notification> queryList(int pageNo, int pageSize) {
        String appId = TokenHandler.getAppId();
        Page<Notification> page = new Page<>(pageNo, pageSize);
        Page<Notification> pageData = notificationMapper.selectListForRecord(appId, page);
        return new PageData<Notification>().praise(pageData);
    }

    @Override
    public NotificationRule verifyRuleCode(String ruleCode) {
        QueryWrapper<NotificationRule> wrapper = new QueryWrapper<NotificationRule>().eq("code", ruleCode);
        return notificationRuleMapper.selectOne(wrapper);
    }

    @Override
    public void checkMsgStatus(int nid, String tid) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger count = new AtomicInteger(0);

        Notification notification = notificationMapper.selectById(nid);
        if (notification == null) {
            log.warn("Notification not exist: nid={}", nid);
            return;
        }
        Runnable task = () -> {
            if (count.incrementAndGet() < 5) {
                List<Log> logs = logRepository.findByTraceId(tid);
                for (Log l : logs) {
                    String message = l.getMessage();
                    if (message.startsWith(LogField.FEIGN_TRACER)) {
                        int status = Integer.parseInt(message.substring(message.length() - 3));
                        notification.setStatus(status);
                        notificationMapper.updateById(notification);
                        log.info("Notification trace succeed, cancel scheduled task");
                        executor.shutdown();
                        return;
                    }
                }
            } else {
                notification.setStatus(-1);
                notificationMapper.updateById(notification);
                log.info("Notification trace time out, cancel scheduled task");
                executor.shutdown();
            }
        };
        executor.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
    }

    @Override
    public List<NotificationRule> queryRulesByIds(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return notificationRuleMapper.selectBatchIds(idList);
    }
}
