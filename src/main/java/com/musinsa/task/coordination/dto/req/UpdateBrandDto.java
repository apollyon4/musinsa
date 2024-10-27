package com.musinsa.task.coordination.dto.req;

import com.musinsa.task.coordination.enums.BrandStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBrandDto {
    private Long id;
    private String name;
    private BrandStatus status;
}
