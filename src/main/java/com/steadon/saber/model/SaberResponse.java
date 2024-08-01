package com.steadon.saber.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.steadon.saber.common.LogField;
import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaberResponse<T> implements Serializable {

    private long traceId;
    private ResultCode resultCode;
    private String message;
    private T data;
    private long serverTime;

    public SaberResponse() {
        String traceId = MDC.get(LogField.TRACE_ID);
        this.traceId = traceId == null ? -1 : Long.parseLong(traceId);
        this.serverTime = System.currentTimeMillis();
        this.resultCode = ResultCode.SUCCESS;
    }

    public SaberResponse(T data) {
        this();
        this.data = data;
    }

    public SaberResponse(ResultCode resultCode, String message) {
        this();
        this.resultCode = resultCode;
        this.message = message;
    }

    public SaberResponse(ResultCode resultCode, String message, T data) {
        this();
        this.data = data;
        this.resultCode = resultCode;
        this.message = message;
    }

    public SaberResponse(Exception e) {
        this(ResultCode.FAILURE, e.getMessage());
    }

    public static <T> SaberResponse<T> success() {
        return new SaberResponse<>();
    }

    public static <T> SaberResponse<T> success(T data) {
        return new SaberResponse<>(data);
    }

    public static <T> SaberResponse<T> failure(String message) {
        return new SaberResponse<>(ResultCode.FAILURE, message);
    }

    public static <T> SaberResponse<T> failure(ResultCode resultCode, String message) {
        return new SaberResponse<>(resultCode, message);
    }

    public static <T> SaberResponse<T> appNotExist() {
        return new SaberResponse<>(ResultCode.INVALID_PARAM, "业务方不存在");
    }

    public static <T> SaberResponse<T> appHasExist() {
        return new SaberResponse<>(ResultCode.INVALID_PARAM, "业务方已存在");
    }
}
