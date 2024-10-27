package com.musinsa.task.coordination.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musinsa.task.coordination.enums.BrandStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBrandDto {
    @JsonIgnore
    private Long id;
    private String name;
    private BrandStatus status;
}
