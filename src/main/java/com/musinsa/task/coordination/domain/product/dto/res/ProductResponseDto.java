package com.musinsa.task.coordination.domain.product.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {
    private Long id;
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("브랜드")
    private String brandName;
    @JsonProperty("가격")
    private Long price;
    private ProductStatus status;

    public String getPrice() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
