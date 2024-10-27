package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(long id) {
        super("product id " + id + " is not exists.", ErrorCode.ENTITY_NOT_FOUND);
    }
}
