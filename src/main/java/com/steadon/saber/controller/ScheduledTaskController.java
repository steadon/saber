package com.steadon.saber.controller;

import com.steadon.saber.biz.IScheduledTaskBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ScheduledTaskParam;
import com.steadon.saber.model.param.SearchScheduledTaskParam;
import com.steadon.saber.model.result.ScheduleTaskResult;
import com.steadon.saber.model.result.ScheduledTaskInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduled/task")
public class ScheduledTaskController {

    private final IScheduledTaskBiz iScheduledTaskBiz;

    /**
     * 分页获取定时任务
     */
    @PostMapping("/list")
    public SaberResponse<PageData<ScheduledTaskInfo>> list(@RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                           @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestBody(required = false) SearchScheduledTaskParam param) {
        return iScheduledTaskBiz.list(pageNo, pageSize, param);
    }

    /**
     * 新增定时任务
     */
    @PostMapping("/add")
    public SaberResponse<ScheduleTaskResult> add(@RequestBody ScheduledTaskParam param) {
        return iScheduledTaskBiz.add(param);
    }

    /**
     * 删除定时任务
     */
    @PostMapping("/delete")
    public SaberResponse<String> delete(@RequestParam(value = "id") int id) {
        return iScheduledTaskBiz.delete(id);
    }

    /**
     * 更新定时任务
     */
    @PostMapping("/update")
    public SaberResponse<ScheduleTaskResult> update(@RequestBody ScheduledTaskParam param) {
        return iScheduledTaskBiz.update(param);
    }
}
