package com.musinsa.task.coordination.domain.product.exception;

import com.musinsa.task.coordination.error.ErrorCode;
import com.musinsa.task.coordination.error.exception.BusinessException;

public class CategoryHasNoProductsException extends BusinessException {
    public CategoryHasNoProductsException(String categoryName) {
        super(categoryName + " has no products.", ErrorCode.CATEGORY_HAS_NO_PRODUCTS);
    }
}
