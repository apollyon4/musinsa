package com.musinsa.task.coordination.domain.brand.dto.req;

import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandUpdateDto {
    private Long id;
    private String name;
    private BrandStatus status;
}
