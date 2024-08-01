package com.steadon.saber.model.result;

import lombok.Data;

@Data
public class LogHistogramResult {
    private Integer infoCount;
    private Integer warnCount;
    private Integer errorCount;
}
