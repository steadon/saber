package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.ScheduledTaskParam;
import com.steadon.saber.model.param.SearchScheduledTaskParam;
import com.steadon.saber.model.result.ScheduleTaskResult;
import com.steadon.saber.model.result.ScheduledTaskInfo;

public interface IScheduledTaskBiz {

    SaberResponse<PageData<ScheduledTaskInfo>> list(int pageNo, int pageSize, SearchScheduledTaskParam param);

    SaberResponse<ScheduleTaskResult> add(ScheduledTaskParam param);

    SaberResponse<String> delete(int id);

    SaberResponse<ScheduleTaskResult> update(ScheduledTaskParam param);
}
