package com.musinsa.task.coordination.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandCreateDto {
    @NotNull
    private String name;
}
