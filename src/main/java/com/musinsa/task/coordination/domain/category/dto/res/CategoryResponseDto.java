package com.musinsa.task.coordination.domain.category.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {
    @JsonProperty("카테고리")
    private String categoryName;
}
