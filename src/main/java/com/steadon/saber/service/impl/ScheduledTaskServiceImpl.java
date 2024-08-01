package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.annotation.AppIsolated;
import com.steadon.saber.common.AdminField;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.model.dto.ScheduledTaskDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchScheduledTaskParam;
import com.steadon.saber.repository.mapper.ScheduledTaskMapper;
import com.steadon.saber.service.ScheduledTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final ScheduledTaskMapper scheduledTaskMapper;

    @Override
    @AppIsolated
    public PageData<ScheduledTask> queryRuleList(Integer pageNo, Integer pageSize) {
        QueryWrapper<ScheduledTask> wrapper = new QueryWrapper<>();
        if (!AdminField.ROOT.equals(TokenHandler.getAppId())) {
            wrapper.eq("app_id", TokenHandler.getAppId());
        }
        wrapper.orderByAsc("task_time");
        Page<ScheduledTask> page = new Page<>(pageNo, pageSize);
        IPage<ScheduledTask> pageData = scheduledTaskMapper.selectPage(page, wrapper);
        return new PageData<ScheduledTask>().praise(pageData);
    }

    @Override
    public List<ScheduledTask> queryRuleListByTime(LocalDateTime currentTime, LocalDateTime thirtyMinutesLater) {
        return scheduledTaskMapper.selectList(
                new QueryWrapper<ScheduledTask>()
                        .between("task_time", currentTime, thirtyMinutesLater)
                        .orderByAsc("task_time")
        );
    }

    @Override
    public List<ScheduledTaskDto> queryListBySift(SearchScheduledTaskParam siftRequest) {
        String username = TokenHandler.getUsername();
        return scheduledTaskMapper.queryListBySift(siftRequest, username);
    }

    @Override
    public void addRule(ScheduledTask scheduledTask) {
        if (scheduledTaskMapper.insert(scheduledTask) != 1) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deletedRuleById(int id) {
        if (scheduledTaskMapper.deleteById(id) != 1) {
            throw new SaberDBException();
        }
    }

    @Override
    public ScheduledTask queryRuleById(int id) {
        return scheduledTaskMapper.selectById(id);
    }

    @Override
    public void updateRule(ScheduledTask scheduledTask) {
        if (scheduledTaskMapper.updateById(scheduledTask) != 1) {
            throw new SaberDBException();
        }
    }

    @Override
    public void batchUpdateRule(List<ScheduledTask> scheduledTaskList) {
        if (Objects.nonNull(scheduledTaskList)) {
            scheduledTaskMapper.batchUpdate(scheduledTaskList);
        }
    }
}
