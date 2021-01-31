package com.epam.esm.error;

public enum ErrorCode {
    RESOURCE_NOT_FOUND(40444),
    INTERNAL_SERVER_ERROR(50055);

    private int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
