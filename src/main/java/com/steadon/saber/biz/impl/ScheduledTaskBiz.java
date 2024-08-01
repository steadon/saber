package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IScheduledTaskBiz;
import com.steadon.saber.handler.factory.ScheduledFactory;
import com.steadon.saber.handler.helper.AuthHelper;
import com.steadon.saber.handler.scheduled.ScheduledHandler;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.model.dto.ScheduledTaskDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ScheduledTaskParam;
import com.steadon.saber.model.param.SearchScheduledTaskParam;
import com.steadon.saber.model.result.ScheduleTaskResult;
import com.steadon.saber.model.result.ScheduledTaskInfo;
import com.steadon.saber.service.NotificationService;
import com.steadon.saber.service.ScheduledTaskService;
import com.steadon.saber.service.UserGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduledTaskBiz implements IScheduledTaskBiz {

    private final UserGroupService userGroupService;
    private final ScheduledTaskService scheduledTaskService;
    private final NotificationService notificationService;

    private final ScheduledHandler scheduledHandler;

    @Override
    public SaberResponse<PageData<ScheduledTaskInfo>> list(int pageNo, int pageSize, SearchScheduledTaskParam param) {
        if (param != null) {
            return listWithSift(pageNo, pageSize, param);
        } else {
            return listWithOutSift(pageNo, pageSize);
        }
    }

    private SaberResponse<PageData<ScheduledTaskInfo>> listWithSift(int pageNo, int pageSize, SearchScheduledTaskParam param) {
        List<ScheduledTaskDto> scheduledTaskDtoList = scheduledTaskService.queryListBySift(param);
        int totalTasks = scheduledTaskDtoList.size();

        List<ScheduledTaskInfo> taskInfoList = scheduledTaskDtoList.stream()
                .map(ScheduledFactory::scheduledTaskDtoToInfo)
                .collect(Collectors.toList());

        Set<Integer> groupIdList = taskInfoList.stream()
                .map(ScheduledTaskInfo::getGroupId)
                .collect(Collectors.toSet());

        Map<Integer, Group> groupMap = userGroupService.queryGroupsByIdList(new ArrayList<>(groupIdList))
                .stream()
                .collect(Collectors.toMap(Group::getId, Function.identity()));

        taskInfoList.forEach(taskInfo -> {
            Group group = groupMap.get(taskInfo.getGroupId());
            if (group != null) {
                taskInfo.setGoal(group.getName());
            }
        });

        PageData<ScheduledTaskInfo> pageData = new PageData<>(
                taskInfoList,
                (long) totalTasks,
                (long) pageNo,
                (long) pageSize,
                ceil(totalTasks, pageSize).longValue()
        );
        return SaberResponse.success(pageData);
    }

    private SaberResponse<PageData<ScheduledTaskInfo>> listWithOutSift(int pageNo, int pageSize) {
        PageData<ScheduledTask> pageData = scheduledTaskService.queryRuleList(pageNo, pageSize);

        List<ScheduledTask> scheduledTaskList = pageData.getResultList();

        List<Integer> ruleIds = scheduledTaskList.stream()
                .map(ScheduledTask::getRuleId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, NotificationRule> notificationRuleMap = notificationService.queryRulesByIds(ruleIds)
                .stream()
                .collect(Collectors.toMap(NotificationRule::getId, Function.identity()));

        Set<Integer> groupIdList = notificationRuleMap.values().stream()
                .map(NotificationRule::getGroupId)
                .collect(Collectors.toSet());
        Map<Integer, Group> groupMap = userGroupService.queryGroupsByIdList(new ArrayList<>(groupIdList))
                .stream()
                .collect(Collectors.toMap(Group::getId, Function.identity()));

        List<ScheduledTaskInfo> taskInfoList = scheduledTaskList.stream()
                .map(v -> {
                    NotificationRule notificationRule = notificationRuleMap.get(v.getRuleId());
                    if (notificationRule != null) {
                        return ScheduledFactory.scheduledTaskToInfo(v, notificationRule, groupMap.get(notificationRule.getGroupId()));
                    }
                    return new ScheduledTaskInfo();
                }).toList();

        return SaberResponse.success(PageData.convert(pageData, taskInfoList));
    }

    @Override
    public SaberResponse<ScheduleTaskResult> add(ScheduledTaskParam param) {
        if (param.getTaskTime() > param.getEndTime()
                || param.getStartTime() > param.getEndTime()
                || param.getTaskTime() <= System.currentTimeMillis() / 1000 + 600) {
            return SaberResponse.failure("任务时间不合法");
        }
        NotificationRule rule = notificationService.queryRuleByCode(param.getNotification().getCode());
        if (rule == null) {
            return SaberResponse.failure("rule_code不存在");
        }
        String username = TokenHandler.getUsername();
        String appId = TokenHandler.getAppId();
        ScheduledTask scheduledTask = ScheduledFactory.scheduledTaskRequestToTask(param, null, appId, rule.getId());
        scheduledTask.setCreateBy(username);
        scheduledTask.setUpdateBy(username);
        scheduledTaskService.addRule(scheduledTask);
        return SaberResponse.success(ScheduledFactory.scheduledTaskToResult(scheduledTask));
    }

    @Override
    public SaberResponse<String> delete(int taskId) {
        ScheduledTask task = scheduledTaskService.queryRuleById(taskId);
        if (task == null) {
            return SaberResponse.failure("定时任务不存在或已删除");
        }
        ScheduledHandler.deleteScheduledTask(task.getUuid());
        scheduledTaskService.deletedRuleById(taskId);
        return SaberResponse.success("删除成功");
    }

    @Override
    public SaberResponse<ScheduleTaskResult> update(ScheduledTaskParam param) {
        if (param.getTaskTime() > param.getEndTime()
                || param.getStartTime() > param.getEndTime()
                || param.getTaskTime() <= System.currentTimeMillis() / 1000 + 600) {
            return SaberResponse.failure("任务时间不合法");
        }
        ScheduledTask task = scheduledTaskService.queryRuleById(param.getId());
        if (task == null) {
            return SaberResponse.failure("定时任务不存在或已删除");
        }
        AuthHelper.checkPermission(task.getAppId());
        NotificationRule rule = notificationService.queryRuleByCode(param.getNotification().getCode());
        if (rule == null) {
            return SaberResponse.failure("rule_code不存在");
        }
        ScheduledTask updatedTask = ScheduledFactory.scheduledTaskRequestToTask(param, task.getUuid(), task.getAppId(), rule.getId());
        updatedTask.setUpdateBy(TokenHandler.getUsername());
        scheduledHandler.updateScheduledTask(updatedTask);
        return SaberResponse.success(ScheduledFactory.scheduledTaskToResult(updatedTask));
    }

    private Integer ceil(Integer a, Integer b) {
        return (a + b - 1) / b;
    }
}
