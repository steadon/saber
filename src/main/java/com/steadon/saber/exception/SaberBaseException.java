package com.steadon.saber.exception;

import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.Getter;

@Getter
public class SaberBaseException extends RuntimeException {

    private final ResultCode code;

    public SaberBaseException(String message) {
        super(message);
        this.code = ResultCode.FAILURE;
    }

    public SaberBaseException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

    public SaberBaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.FAILURE;
    }

    public SaberBaseException(ResultCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}