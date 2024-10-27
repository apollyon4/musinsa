package com.musinsa.task.coordination.dto.req;

import com.musinsa.task.coordination.enums.ProductStatus;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDto {
    private Long id;
    @PositiveOrZero
    private Long price;
    private ProductStatus status;
}
