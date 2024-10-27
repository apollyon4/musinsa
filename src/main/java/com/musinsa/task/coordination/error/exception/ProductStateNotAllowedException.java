package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class ProductStateNotAllowedException extends InvalidValueException {
    public ProductStateNotAllowedException(String origin, String target) {
        super(origin + " to " + target + "not allowed.", ErrorCode.PRODUCT_STATE_UPDATE_NOT_ALLOWED);
    }
}
