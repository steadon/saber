package com.steadon.saber.service;

import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.result.LogHistogramResult;

import java.util.List;

public interface LogService {

    List<Log> getLogList(String keyword, String startLogId, String endLogId, Long startTime, Long endTime, String level);

    LogHistogramResult getHistogramByTimeStamp(Long timeStamp);
}
