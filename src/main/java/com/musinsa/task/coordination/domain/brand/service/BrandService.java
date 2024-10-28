package com.musinsa.task.coordination.domain.brand.service;

import com.musinsa.task.coordination.domain.brand.dto.req.BrandCreateDto;
import com.musinsa.task.coordination.domain.brand.dto.req.BrandUpdateDto;
import com.musinsa.task.coordination.domain.brand.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.BrandNotFoundException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandStatusManager brandStatusManager;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final ProductRepository productRepository;

    public BrandResponseDto addBrand(BrandCreateDto brandCreateDto) {
        Brand brand = brandRepository.save(Brand.builder()
                .name(brandCreateDto.getName())
                .status(BrandStatus.STANDBY)
                .build());
        brand = brandRepository.save(brand);
        return BrandResponseDto.from(brand);
    }

    @Transactional
    public BrandResponseDto updateBrand(BrandUpdateDto brandUpdateDto) {
        Brand brand = brandRepository.findById(brandUpdateDto.getId())
                .orElseThrow(() -> new BrandNotFoundException(brandUpdateDto.getId()));

        if (ObjectUtils.isNotEmpty(brandUpdateDto.getName())) {
            brand.setName(brandUpdateDto.getName());
        }
        if (ObjectUtils.isNotEmpty(brandUpdateDto.getStatus())) {
            brandStatusManager.changeStatus(brand, brandUpdateDto.getStatus());
        }
        return BrandResponseDto.from(brandRepository.save(brand));
    }

    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
        brandStatusManager.changeStatus(brand, BrandStatus.REMOVED);
        brandRepository.save(brand);
    }

    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll()
                .stream()
                .filter(brand -> !BrandStatus.REMOVED.equals(brand.getStatus()))
                .map(BrandResponseDto::from)
                .toList();
    }

    public BrandResponseDto getBrand(Long brandId) {
        return BrandResponseDto.from(brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId)));
    }
}
