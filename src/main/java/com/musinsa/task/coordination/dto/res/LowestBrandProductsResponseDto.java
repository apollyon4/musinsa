package com.musinsa.task.coordination.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LowestBrandProductsResponseDto {
    private String brandName;
    private List<ProductResponseDto> products;
    private long totalPrice;
}
