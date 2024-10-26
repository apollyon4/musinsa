package com.musinsa.task.coordination.dto.res;

import com.musinsa.task.coordination.entity.Brand;
import lombok.Getter;

@Getter
public class BrandResponseDto {
    private Long id;
    private String name;
    private String status;

    public static BrandResponseDto from(Brand brand) {
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        brandResponseDto.id = brand.getId();
        brandResponseDto.name = brand.getName();
        brandResponseDto.status = brand.getStatus().name();
        return brandResponseDto;
    }
}
