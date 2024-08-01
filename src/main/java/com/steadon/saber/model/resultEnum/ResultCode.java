package com.steadon.saber.model.resultEnum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResultCode {
    SUCCESS("success"),
    FAILURE("failure"),
    INVALID_PARAM("invalid_param"),
    NO_PERMISSION("no_permission"),
    DB_ERROR("db_error"),
    RATE_LIMIT("rate_limit");

    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
