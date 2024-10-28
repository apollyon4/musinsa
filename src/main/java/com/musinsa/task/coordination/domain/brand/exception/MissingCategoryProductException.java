package com.musinsa.task.coordination.domain.brand.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class MissingCategoryProductException extends BusinessException {
    public MissingCategoryProductException(long id) {
        super("brand id " + id + " has missing some category.", ErrorCode.IncompleteCategoryRegistrationException);
    }
}
