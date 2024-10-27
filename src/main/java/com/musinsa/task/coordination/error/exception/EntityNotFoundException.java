package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
