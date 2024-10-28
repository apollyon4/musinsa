package com.musinsa.task.coordination.domain.product.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateDto {
    @JsonIgnore
    private Long id;
    @PositiveOrZero
    private Long price;
    private ProductStatus status;
}
