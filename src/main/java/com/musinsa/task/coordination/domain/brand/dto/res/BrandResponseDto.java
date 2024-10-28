package com.musinsa.task.coordination.domain.brand.dto.res;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
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
        if (brand.getStatus() != null) {
            brandResponseDto.status = brand.getStatus().name();
        }
        return brandResponseDto;
    }
}
