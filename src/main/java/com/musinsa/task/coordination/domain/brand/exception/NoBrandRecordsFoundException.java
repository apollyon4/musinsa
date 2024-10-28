package com.musinsa.task.coordination.domain.brand.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class NoBrandRecordsFoundException extends BusinessException {
    public NoBrandRecordsFoundException(String message) {
        super(message, ErrorCode.NO_BRAND_RECORDS_FOUND);
    }
}
