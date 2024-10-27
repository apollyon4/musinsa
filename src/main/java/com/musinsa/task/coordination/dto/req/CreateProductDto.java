package com.musinsa.task.coordination.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductDto {
    private String name;
    private Long price;
    private Long brandId;
    private Long categoryId;
}
