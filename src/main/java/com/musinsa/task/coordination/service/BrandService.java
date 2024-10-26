package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

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
        brand.setStatus(updateBrandDto.getStatus());
        return BrandResponseDto.from(brandRepository.save(brand));
    }

    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));
        brand.setStatus(BrandStatus.REMOVED);
        // TODO : 상품의 상태 정보를 변경하는 로직이 필요합니다.

        brandRepository.save(brand);
    }
}
