package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public BrandResponseDto updateBrand(UpdateBrandDto updateBrandDto) {
        if (BrandStatus.STANDBY.equals(updateBrandDto.getStatus())) {
            throw new IllegalArgumentException("STANDBY 상태로 변경할 수 없습니다.");
        }

        Brand brand = brandRepository.findById(updateBrandDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));

        if (ObjectUtils.isNotEmpty(updateBrandDto.getName())) {
            brand.setName(updateBrandDto.getName());
        }
        if (ObjectUtils.isNotEmpty(updateBrandDto.getStatus())) {
            changeStatus(brand, updateBrandDto.getStatus());
        }
        return BrandResponseDto.from(brandRepository.save(brand));
    }

    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));
        changeStatus(brand, BrandStatus.REMOVED);
        brandRepository.save(brand);
    }

    private void changeStatus(Brand brand, BrandStatus status) {
        if (!brand.getStatus().isMoveable(status)) {
            throw new IllegalArgumentException("요청한 상태로 변경 불가능합니다.");
        }

        if (BrandStatus.ACTIVATE.equals(status)) {
            if (!productRepositoryCustom.hasProductsInAllCategories(brand.getId())) {
                throw new IllegalArgumentException("모든 카테고리에 상품이 등록되어 있어야 합니다.");
            }
            // change product if stop -> activate
            productRepositoryCustom.updateProductStatusByBrandId(brand.getId(), ProductStatus.BLOCKED, ProductStatus.ACTIVATE);
            productRepositoryCustom.updateProductStatusByBrandId(brand.getId(), ProductStatus.STANDBY, ProductStatus.ACTIVATE);
        } else if (BrandStatus.STOP.equals(status)) {
            // change product if activate -> blocked
            productRepositoryCustom.updateProductStatusByBrandId(brand.getId(), ProductStatus.ACTIVATE, ProductStatus.BLOCKED);
        } else if (BrandStatus.REMOVED.equals(status)) {
            // change all products to removed
            productRepositoryCustom.removeProductsByBrandId(brand.getId());
        }
        brand.setStatus(status);
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
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다.")));
    }
}
