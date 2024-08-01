package com.steadon.saber.handler.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steadon.saber.common.TimeCommon;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.model.dto.ScheduledTaskDto;
import com.steadon.saber.model.param.NotificationParam;
import com.steadon.saber.model.param.ScheduledTaskParam;
import com.steadon.saber.model.result.ScheduleTaskResult;
import com.steadon.saber.model.result.ScheduledTaskInfo;
import com.steadon.saber.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
public class ScheduledFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static NotificationParam taskToRequest(ScheduledTask scheduledTask) {
        String attrsJson = scheduledTask.getAttrs();
        return objectMapper.readValue(attrsJson, NotificationParam.class);
    }

    public static ScheduledTaskInfo scheduledTaskToInfo(ScheduledTask task, NotificationRule nRule, Group group) {
        ScheduledTaskInfo info = new ScheduledTaskInfo();
        info.setId(task.getId());
        info.setType(task.getTaskType());
        info.setTaskTime(task.getTaskTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setStartTime(task.getStartTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setEndTime(task.getEndTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setCreateBy(task.getCreateBy());
        info.setCreateTime(task.getCreateTime().toEpochSecond(TimeCommon.zoneOffset));

        info.setRuleCode(nRule.getCode());
        info.setChannel(new ScheduledTaskInfo.Channel(nRule));
        info.setGroupId(nRule.getGroupId());

        if (group != null) {
            info.setGoal(group.getName());
        }
        return info;
    }

    public static ScheduledTaskInfo scheduledTaskDtoToInfo(ScheduledTaskDto taskDto) {
        ScheduledTaskInfo info = new ScheduledTaskInfo();
        info.setId(taskDto.getId());
        info.setRuleCode(taskDto.getRuleCode());
        info.setChannel(new ScheduledTaskInfo.Channel(taskDto.getFeishu(), taskDto.getSms(), taskDto.getEmail()));
        info.setTaskTime(taskDto.getTaskTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setStartTime(taskDto.getStartTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setEndTime(taskDto.getEndTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setCreateTime(taskDto.getCreateTime().toEpochSecond(TimeCommon.zoneOffset));
        info.setCreateBy(taskDto.getCreateBy());
        info.setType(taskDto.getTaskType());
        return info;
    }


    /**
     * scheduledTaskRequest请求参数类转换为ScheduledTask数据库实体对象映射类
     *
     * @param request scheduledTaskRequest请求参数类
     * @return ScheduledTask ScheduledTask数据库实体对象映射类
     * @see ScheduledTaskParam
     * @see ScheduledTask
     */
    @SneakyThrows
    public static ScheduledTask scheduledTaskRequestToTask(ScheduledTaskParam request, String uuid, String appId, Integer ruleId) {
        ScheduledTask scheduledTask = new ScheduledTask();

        scheduledTask.setId(request.getId());
        scheduledTask.setTaskType(request.getTaskType());
        scheduledTask.setTaskTime(LocalDateTime.ofEpochSecond(request.getTaskTime(), 0, TimeCommon.zoneOffset));
        scheduledTask.setStartTime(LocalDateTime.ofEpochSecond(request.getStartTime(), 0, TimeCommon.zoneOffset));
        scheduledTask.setEndTime(LocalDateTime.ofEpochSecond(request.getEndTime(), 0, TimeCommon.zoneOffset));
        scheduledTask.setRuleId(ruleId);
        scheduledTask.setUuid(uuid);
        if (StringUtils.isNotEmpty(appId)) {
            scheduledTask.setAppId(appId);
        }
        scheduledTask.setAttrs(objectMapper.writeValueAsString(request.getNotification()));
        return scheduledTask;
    }

    public static ScheduleTaskResult scheduledTaskToResult(ScheduledTask scheduledTask) {
        ScheduleTaskResult result = new ScheduleTaskResult();
        result.setTaskTime(scheduledTask.getTaskTime().toEpochSecond(TimeCommon.zoneOffset));
        result.setStartTime(scheduledTask.getStartTime().toEpochSecond(TimeCommon.zoneOffset));
        result.setEndTime(scheduledTask.getEndTime().toEpochSecond(TimeCommon.zoneOffset));
        if (Objects.nonNull(scheduledTask.getCreateTime())) {
            result.setCreateTime(scheduledTask.getCreateTime().toEpochSecond(TimeCommon.zoneOffset));
        }
        if (Objects.nonNull(scheduledTask.getUpdateTime())) {
            result.setUpdateTime(scheduledTask.getUpdateTime().toEpochSecond(TimeCommon.zoneOffset));
        }
        result.setTaskType(scheduledTask.getTaskType());
        result.setAppId(scheduledTask.getAppId());
        result.setAttrs(scheduledTask.getAttrs());
        result.setUuid(scheduledTask.getUuid());
        result.setCreateBy(scheduledTask.getCreateBy());
        result.setUpdateBy(scheduledTask.getUpdateBy());
        return result;
    }
}
