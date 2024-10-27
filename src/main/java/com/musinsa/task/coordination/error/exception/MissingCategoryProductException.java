package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class MissingCategoryProductException extends BusinessException {
    public MissingCategoryProductException(long id) {
        super("brand id " + id + " has missing some category.", ErrorCode.IncompleteCategoryRegistrationException);
    }
}
