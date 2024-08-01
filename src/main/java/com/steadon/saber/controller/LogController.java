package com.steadon.saber.controller;

import com.steadon.saber.annotation.LogIgnored;
import com.steadon.saber.biz.ILogBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.result.LogHistogramResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志模块
 */
@LogIgnored
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class LogController {

    private final ILogBiz iLogBiz;

    /**
     * 获得日志
     */
    @GetMapping("/log")
    public SaberResponse<List<Log>> getLogList(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                               @RequestParam(value = "start_log_id", required = false, defaultValue = "") String startLogId,
                                               @RequestParam(value = "end_log_id", required = false, defaultValue = "") String endLogId,
                                               @RequestParam(value = "start_time", required = false) Long startTime,
                                               @RequestParam(value = "end_time", required = false) Long endTime,
                                               @RequestParam(value = "level", required = false, defaultValue = "info") String level) {
        return iLogBiz.getLogList(keyword, startLogId, endLogId, startTime, endTime, level);
    }

    /**
     * 获得日志信息图表
     */
    @GetMapping("/log/histogram")
    public SaberResponse<List<LogHistogramResult>> getHistogram() {
        return iLogBiz.getHistogram();
    }
}
