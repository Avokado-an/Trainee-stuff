package com.epam.esm.error;

public class ErrorHandler {
    private String errorMessage;
    private int errorCode;

    public ErrorHandler(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
