package com.musinsa.task.coordination.domain.product.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LowestPriceStyleResponseDto {
    @JsonProperty("최저가")
    private ProductListResponseDto productListResponseDto;

    public LowestPriceStyleResponseDto(ProductListResponseDto productListResponseDto) {
        this.productListResponseDto = productListResponseDto;
    }
}
