package com.musinsa.task.coordination.domain.product.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class LastProductInBrandCategoryException extends BusinessException {
    public LastProductInBrandCategoryException(Long brandId, Long categoryId) {
        super("Last product in brand id " + brandId + " category id " + categoryId + ".", ErrorCode.LAST_PRODUCT_IN_BRAND_CATEGORY);
    }
}
