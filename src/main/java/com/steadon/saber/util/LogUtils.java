package com.steadon.saber.util;

import com.steadon.saber.model.dao.Log;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtils {
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s(\\S+)\\s*---\\s*\\[([^]]+)]\\s([^:]+):\\s*\\[TID=([^]]+)](.*)",
            Pattern.DOTALL);

    public static Log praiseLogStr(String logStr) {
        Matcher matcher = LOG_PATTERN.matcher(logStr);
        if (!matcher.find()) {
            return null;
        }

        String time = matcher.group(1);
        String level = matcher.group(2).trim();
        String thread = matcher.group(3).trim();
        String method = matcher.group(4).trim();
        String tid = matcher.group(5).trim();
        String message = matcher.group(6).trim();

        if ("N/A".equals(tid)) {
            return null;
        }

        Log log = new Log();
        log.setTraceId(tid);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        long timestamp = LocalDateTime.parse(time, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        log.setTimestamp(timestamp);
        log.setLevel(level);
        log.setThread(thread);
        log.setMethod(method);
        log.setMessage(message);
        log.setCreatedAt(new Date(timestamp));
        return log;
    }
}
