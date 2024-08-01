package com.steadon.saber.exception;

import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.Getter;

@Getter
public class SaberInvalidException extends SaberBaseException {

    public SaberInvalidException(String message) {
        super(ResultCode.INVALID_PARAM, message);
    }
}
