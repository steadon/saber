package com.steadon.saber.exception;

import com.steadon.saber.model.resultEnum.ResultCode;
import lombok.Getter;

@Getter
public class SaberDBException extends SaberBaseException {

    public SaberDBException() {
        super(ResultCode.DB_ERROR, "数据库异常");
    }
}