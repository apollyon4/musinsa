package com.musinsa.task.coordination.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBrandDto {
    @NotNull
    private String name;
}
