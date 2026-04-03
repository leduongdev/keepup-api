package com.ra.base_spring_boot.exception;

import com.ra.base_spring_boot.model.enums.ErrorCode;

public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        // Truyền message từ Enum lên lớp cha RuntimeException
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}