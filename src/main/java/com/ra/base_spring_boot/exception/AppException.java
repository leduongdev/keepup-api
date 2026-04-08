package com.ra.base_spring_boot.exception;

import com.ra.base_spring_boot.model.enums.ErrorCode;

public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String field;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.field = null;
    }

    public AppException(ErrorCode errorCode, String field) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.field = field;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }
}