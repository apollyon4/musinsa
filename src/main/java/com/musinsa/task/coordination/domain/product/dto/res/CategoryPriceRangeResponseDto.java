package com.musinsa.task.coordination.domain.product.dto.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.task.coordination.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CategoryPriceRangeResponseDto {
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("최저가")
    private ProductResponseDto lowestProduct;
    @JsonProperty("최고가")
    private ProductResponseDto highestProduct;

    public List<ProductResponseDto> getLowestProduct() {
        return Collections.singletonList(lowestProduct);
    }

    public List<ProductResponseDto> getHighestProduct() {
        return Collections.singletonList(highestProduct);
    }

    public CategoryPriceRangeResponseDto(String categoryName, Product lowestProduct, Product highestProduct) {
        this.categoryName = categoryName;
        this.lowestProduct = ProductResponseDto.builder()
                .brandName(lowestProduct.getBrand().getName())
                .price(lowestProduct.getPrice())
                .build();
        this.highestProduct = ProductResponseDto.builder()
                .brandName(highestProduct.getBrand().getName())
                .price(highestProduct.getPrice())
                .build();
    }
}
