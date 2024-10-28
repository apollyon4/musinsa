package com.musinsa.task.coordination.domain.product.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class BrandNotActiveException extends BusinessException {
    public BrandNotActiveException(Long brandId) {
        super("Brand " + brandId + " is not active.", ErrorCode.BRAND_NOT_ACTIVATED);
    }
}
