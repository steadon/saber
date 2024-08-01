package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.ILogBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.result.LogHistogramResult;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.LogService;
import com.steadon.saber.util.StringUtils;
import jakarta.annotation.Resource;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LogBiz implements ILogBiz {

    private static final Set<String> VALID_LOG_LEVELS = Set.of("INFO", "WARN", "ERROR");

    private static final long multi = 3600000L;

    @Resource
    private LogService logService;

    @Override
    public SaberResponse<List<Log>> getLogList(String keyword, String startLogId, String endLogId, Long startTime, Long endTime, String level) {
        if (!VALID_LOG_LEVELS.contains(level.toUpperCase())) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "Invalid log level");
        }
        if ((StringUtils.isNotEmpty(startLogId) && !ObjectId.isValid(startLogId)) ||
                (StringUtils.isNotEmpty(endLogId) && !ObjectId.isValid(endLogId))) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "Invalid log id");
        }
        // 不允许只传startTime不传递endTime
        if ((startTime != null && endTime == null)) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "Invalid time");
        }
        List<Log> logList = logService.getLogList(keyword, startLogId, endLogId, startTime, endTime, level);
        return SaberResponse.success(logList);
    }

    @Override
    public SaberResponse<List<LogHistogramResult>> getHistogram() {
        List<LogHistogramResult> ans = new ArrayList<>();
        long curTime = System.currentTimeMillis() / multi * multi;
        for (int i = 0; i < 7; i++) {
            LogHistogramResult histogram = logService.getHistogramByTimeStamp(curTime);
            ans.add(histogram);
            curTime -= 3600000;
        }
        return SaberResponse.success(ans);
    }
}
