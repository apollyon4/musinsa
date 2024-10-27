package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(String categoryName) {
        super("category id " + categoryName + " is not exists.", ErrorCode.ENTITY_NOT_FOUND);
    }
}
