package com.musinsa.task.coordination.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDto {
    private Long id;
    private String name;
    private Long price;
}
