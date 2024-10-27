package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    public BrandResponseDto addBrand(CreateBrandDto createBrandDto) {
        Brand brand = brandRepository.save(Brand.builder()
                .name(createBrandDto.getName())
                .status(BrandStatus.STANDBY)
                .build());
        brand = brandRepository.save(brand);
        return BrandResponseDto.from(brand);
    }

    public BrandResponseDto updateBrand(UpdateBrandDto updateBrandDto) {
        Brand brand = brandRepository.findById(updateBrandDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));
        brand.setName(updateBrandDto.getName());
        // TODO : 브랜드 상태가 변경될 때, 해당 브랜드의 상품 상태도 변경되어야 함
        brand.setStatus(updateBrandDto.getStatus());
        return BrandResponseDto.from(brandRepository.save(brand));
    }

    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));
        brand.setStatus(BrandStatus.REMOVED);
        long count = productRepositoryCustom.updateProductActivateToStopByBrandId(brand.getId());
        brandRepository.save(brand);
    }
}
