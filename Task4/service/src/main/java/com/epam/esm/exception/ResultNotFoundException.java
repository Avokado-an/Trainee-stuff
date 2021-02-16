package com.epam.esm.exception;

public class ResultNotFoundException extends Exception {
    public ResultNotFoundException() {
    }

    public ResultNotFoundException(String message) {
        super(message);
    }

    public ResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultNotFoundException(Throwable cause) {
        super(cause);
    }
}
