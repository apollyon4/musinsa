package com.musinsa.task.coordination.error.exception;

import com.musinsa.task.coordination.error.ErrorCode;

public class LastProductInBrandCategoryException extends BusinessException {
    public LastProductInBrandCategoryException(Long brandId, Long categoryId) {
        super("Last product in brand id " + brandId + " category id " + categoryId + ".", ErrorCode.LAST_PRODUCT_IN_BRAND_CATEGORY);
    }
}
