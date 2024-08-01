package com.steadon.saber.service;

import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.model.dto.ScheduledTaskDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.SearchScheduledTaskParam;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledTaskService {

    PageData<ScheduledTask> queryRuleList(Integer pageNo, Integer pageSize);

    List<ScheduledTask> queryRuleListByTime(LocalDateTime currentTime, LocalDateTime thirtyMinutesLater);

    List<ScheduledTaskDto> queryListBySift(SearchScheduledTaskParam siftRequest);

    void addRule(ScheduledTask scheduledTask);

    void deletedRuleById(int id);

    ScheduledTask queryRuleById(int id);

    void updateRule(ScheduledTask scheduledTask);

    void batchUpdateRule(List<ScheduledTask> scheduledTaskList);
}
