package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.result.LogHistogramResult;

import java.util.List;

public interface ILogBiz {

    SaberResponse<List<Log>> getLogList(String keyword, String startLogId, String endLogId, Long startTime, Long endTime, String level);

    SaberResponse<List<LogHistogramResult>> getHistogram();
}
