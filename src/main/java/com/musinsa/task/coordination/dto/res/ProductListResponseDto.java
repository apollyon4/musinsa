package com.musinsa.task.coordination.dto.res;

import lombok.Getter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductListResponseDto {
    @Getter
    private final List<ProductResponseDto> products;
    private final long total;

    public ProductListResponseDto(List<ProductResponseDto> products) {
        this.products = products;
        this.total = products.stream()
                .reduce(0L, (acc, product) -> acc + product.getPrice(), Long::sum);
    }

    public String getTotal() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(total);
    }
}
