package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
