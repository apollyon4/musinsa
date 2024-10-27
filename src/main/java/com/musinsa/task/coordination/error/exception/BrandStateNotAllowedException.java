package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class BrandStateNotAllowedException extends InvalidValueException {
    public BrandStateNotAllowedException(String origin, String target) {
        super(origin + " to " + target + "not allowed.", ErrorCode.BRAND_STATE_UPDATE_NOT_ALLOWED);
    }
}
