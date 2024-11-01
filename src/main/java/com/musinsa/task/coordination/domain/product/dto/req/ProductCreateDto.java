package com.musinsa.task.coordination.domain.product.dto.req;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDto {
    @PositiveOrZero
    private Long price;
    private Long brandId;
    private Long categoryId;
}
