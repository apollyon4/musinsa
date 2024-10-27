package com.musinsa.task.coordination.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// TODO : name 가져올때 join 시점 등에 대한 검사 필요
public class ProductResponseDto {
    private Long id;
    private String categoryName;
    private String brandName;
    private Long price;
}
