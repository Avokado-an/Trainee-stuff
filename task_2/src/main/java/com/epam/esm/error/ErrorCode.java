package com.epam.esm.error;

public enum ErrorCode {
    RESOURCE_NOT_FOUND(404404);

    private int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
