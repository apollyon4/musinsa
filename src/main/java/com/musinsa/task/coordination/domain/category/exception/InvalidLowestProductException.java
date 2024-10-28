package com.musinsa.task.coordination.domain.category.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class InvalidLowestProductException extends BusinessException {
    public InvalidLowestProductException() {
        super(ErrorCode.INVALID_LOWEST_PRODUCT);
    }
}
