package com.musinsa.task.coordination.domain.brand.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.EntityNotFoundException;

public class BrandNotFoundException extends EntityNotFoundException {
    public BrandNotFoundException(long id) {
        super("brand id " + id + " is not exists.", ErrorCode.ENTITY_NOT_FOUND);
    }
}
