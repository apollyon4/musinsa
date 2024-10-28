package com.musinsa.task.coordination.domain.product.dto.req;

import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateDto {
    private Long id;
    @PositiveOrZero
    private Long price;
    private ProductStatus status;
}
