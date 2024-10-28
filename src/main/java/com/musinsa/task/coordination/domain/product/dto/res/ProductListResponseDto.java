package com.musinsa.task.coordination.domain.product.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.task.coordination.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductListResponseDto {
    @Setter
    @JsonProperty("브랜드")
    private String brandName;
    @JsonProperty("카테고리")
    private List<ProductResponseDto> products;
    @JsonProperty("총액")
    private long totalPrice;

    public ProductListResponseDto(List<Product> products) {
        this.products = products.stream()
                .map(product -> ProductResponseDto.builder()
                        .price(product.getPrice())
                        .categoryName(product.getCategory().getName())
                        .brandName(product.getBrand().getName())
                        .build())
                .collect(Collectors.toList());
        this.totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(0L, Long::sum);
    }

    public String getTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(totalPrice);
    }
}
