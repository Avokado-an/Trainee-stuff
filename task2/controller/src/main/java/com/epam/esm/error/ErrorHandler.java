package com.epam.esm.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorHandler {
    private final String errorMessage;
    private final int errorCode;
}
