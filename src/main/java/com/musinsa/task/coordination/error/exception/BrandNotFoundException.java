package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class BrandNotFoundException extends EntityNotFoundException {
    public BrandNotFoundException(long id) {
        super("brand id " + id + " is not exists.", ErrorCode.ENTITY_NOT_FOUND);
    }
}
