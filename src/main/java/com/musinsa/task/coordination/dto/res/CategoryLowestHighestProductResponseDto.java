package com.musinsa.task.coordination.dto.res;


import lombok.Builder;

@Builder
public class CategoryLowestHighestProductResponseDto {
    private String categoryName;
    private ProductResponseDto lowestProduct;
    private ProductResponseDto highestProduct;
}
