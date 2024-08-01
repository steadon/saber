package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadon.saber.model.dao.ScheduledTask;
import com.steadon.saber.model.dto.ScheduledTaskDto;
import com.steadon.saber.model.param.SearchScheduledTaskParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduledTaskMapper extends BaseMapper<ScheduledTask> {
    int batchUpdate(@Param("list") List<ScheduledTask> scheduledTaskList);

    List<ScheduledTaskDto> queryListBySift(@Param("sift") SearchScheduledTaskParam sift, @Param("createBy") String createBy);
}
