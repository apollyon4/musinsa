package com.musinsa.task.coordination.dto.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class CategoryPriceRangeResponseDto {
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("최저가")
    private ProductResponseDto lowestProduct;
    @JsonProperty("최고가")
    private ProductResponseDto highestProduct;

    public Collection<ProductResponseDto> getLowestProduct() {
        return Collections.singletonList(lowestProduct);
    }

    public Collection<ProductResponseDto> getHighestProduct() {
        return Collections.singletonList(highestProduct);
    }
}
