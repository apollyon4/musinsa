package com.musinsa.task.coordination.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),

    BRAND_STATE_UPDATE_NOT_ALLOWED(400, "B001", "Requested Brand state update is not allowed."),
    NO_BRAND_RECORDS_FOUND(400, "B002", "No Brand records found."),

    PRODUCT_STATE_UPDATE_NOT_ALLOWED(400, "P001", "Requested Brand state update is not allowed."),
    IncompleteCategoryRegistrationException(400, "P002", "Missing some Category in Brand."),
    CATEGORY_HAS_NO_PRODUCTS(400, "P003", "Category has no products."),
    BRAND_NOT_ACTIVATED(400, "P004", "Brand is not activated to update Product."),
    LAST_PRODUCT_IN_BRAND_CATEGORY(400, "P005", "Last product in brand category could not remove."),

    INVALID_LOWEST_PRODUCT(400, "C001", "Invalid lowest product."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
