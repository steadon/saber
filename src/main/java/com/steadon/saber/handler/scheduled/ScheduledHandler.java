package com.steadon.saber.handler.scheduled;

import com.steadon.saber.biz.INotificationBiz;
import com.steadon.saber.common.LogField;
import com.steadon.saber.common.ScheduledTaskType;
import com.steadon.saber.common.TimeCommon;
import com.steadon.saber.handler.factory.ScheduledFactory;
import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.service.ScheduledTaskService;
import com.steadon.saber.util.StringUtils;
import com.steadon.saber.util.TraceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduledHandler {

    private static final Integer SCHEDULED_TASK_INTERVAL = 10;
    private static final Set<String> scheduledTaskUuidDeleteSet = ConcurrentHashMap.newKeySet();
    private static final Set<String> scheduledTaskUuidExistSet = ConcurrentHashMap.newKeySet();

    private static final TemporalAdjuster MONDAY = TemporalAdjusters.next(DayOfWeek.MONDAY);
    private static final TemporalAdjuster TUESDAY = TemporalAdjusters.next(DayOfWeek.TUESDAY);
    private static final TemporalAdjuster WEDNESDAY = TemporalAdjusters.next(DayOfWeek.WEDNESDAY);
    private static final TemporalAdjuster THURSDAY = TemporalAdjusters.next(DayOfWeek.THURSDAY);
    private static final TemporalAdjuster FRIDAY = TemporalAdjusters.next(DayOfWeek.FRIDAY);
    private static final TemporalAdjuster SATURDAY = TemporalAdjusters.next(DayOfWeek.SATURDAY);
    private static final TemporalAdjuster SUNDAY = TemporalAdjusters.next(DayOfWeek.SUNDAY);

    static Map<Integer, Consumer<ScheduledTask>> taskTypeMap = Map.of(
            ScheduledTaskType.MONDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), MONDAY)),
            ScheduledTaskType.TUESDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), TUESDAY)),
            ScheduledTaskType.WEDNESDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), WEDNESDAY)),
            ScheduledTaskType.THURSDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), THURSDAY)),
            ScheduledTaskType.FRIDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), FRIDAY)),
            ScheduledTaskType.SATURDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), SATURDAY)),
            ScheduledTaskType.SUNDAY, t -> t.setTaskTime(transToTimestamp(t.getTaskTime(), SUNDAY)),
            ScheduledTaskType.DAILY, t -> t.setTaskTime(t.getTaskTime().plusDays(1))
    );

    private final INotificationBiz iNotificationBiz;
    private final ScheduledTaskService scheduledTaskService;

    private final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

    public static void deleteScheduledTask(String uuid) {
        if (StringUtils.isNotEmpty(uuid)) {
            scheduledTaskUuidDeleteSet.add(uuid);
            scheduledTaskUuidExistSet.remove(uuid);
        }
    }

    private static LocalDateTime transToTimestamp(LocalDateTime time, TemporalAdjuster adjuster) {
        return time.with(adjuster);
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void scheduledTask() {
        log.info("Scheduled task executed");
        var currentTime = LocalDateTime.now().minusSeconds(1);
        var tenMinutesLater = currentTime.plusMinutes(SCHEDULED_TASK_INTERVAL).plusSeconds(1);
        var tasks = scheduledTaskService.queryRuleListByTime(currentTime, tenMinutesLater);

        tasks = tasks.stream()
                .filter(t -> t.getTaskTime().isBefore(t.getEndTime()) || t.getTaskTime().isEqual(t.getEndTime()))
                .toList();
        tasks.forEach(this::initializeTask);
        tasks.forEach(this::executeScheduledTask);
        updateScheduledTasks(tasks);
    }

    private void initializeTask(ScheduledTask task) {
        task.setUuid(UUID.randomUUID().toString());
        scheduledTaskUuidExistSet.add(task.getUuid());
    }

    public void executeScheduledTask(ScheduledTask task) {
        scheduledThreadPool.schedule(() -> executeTask(task), calculateDelay(task), TimeUnit.MILLISECONDS);
    }

    public void updateScheduledTasks(List<ScheduledTask> tasks) {
        List<ScheduledTask> filteredTasks = tasks.stream()
                .filter(t -> t.getTaskType() != 0)
                .toList();
        for (ScheduledTask t : filteredTasks) {
            Optional.ofNullable(taskTypeMap.get(t.getTaskType())).ifPresent(consumer -> consumer.accept(t));
        }
        // 执行批量更新操作
        if (!filteredTasks.isEmpty()) {
            scheduledTaskService.batchUpdateRule(filteredTasks);
        }
    }

    public void updateScheduledTask(ScheduledTask scheduledTask) {
        if (StringUtils.isNotEmpty(scheduledTask.getUuid()) && scheduledTaskUuidExistSet.contains(scheduledTask.getUuid())) {
            deleteScheduledTask(scheduledTask.getUuid());
        }
        if (calculateDelay(scheduledTask) < SCHEDULED_TASK_INTERVAL) {
            initializeTask(scheduledTask);
            executeScheduledTask(scheduledTask);
        }
        scheduledTaskService.updateRule(scheduledTask);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    private void executeTask(ScheduledTask task) {
        if (!scheduledTaskUuidDeleteSet.remove(task.getUuid())) {
            MDC.put(LogField.TRACE_ID, String.valueOf(TraceUtils.getTraceId()));
            iNotificationBiz.notification(ScheduledFactory.taskToRequest(task));
            scheduledTaskUuidExistSet.remove(task.getUuid());
            log.info("Task execution completed: {}", task);
        }
    }

    /**
     * 计算延迟时间
     *
     * @param task 任务
     * @return 延迟时间
     */
    private long calculateDelay(ScheduledTask task) {
        return System.currentTimeMillis() / 1000 - task.getTaskTime().toEpochSecond(TimeCommon.zoneOffset);
    }
}
