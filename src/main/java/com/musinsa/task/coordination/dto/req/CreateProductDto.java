package com.musinsa.task.coordination.dto.req;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductDto {
    @PositiveOrZero
    private Long price;
    private Long brandId;
    private Long categoryId;
}
