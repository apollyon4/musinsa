package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.entity.Product;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.ProductRepository;
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
    private final ProductRepository productRepository;

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
            this.changeStatus(brand, updateBrandDto.getStatus());
        }
        return BrandResponseDto.from(brandRepository.save(brand));
    }

    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브랜드가 존재하지 않습니다."));
        this.changeStatus(brand, BrandStatus.REMOVED);
        brandRepository.save(brand);
    }

    public void changeStatus(Brand brand, BrandStatus status) {
        brand.changeStatus(status);

        if (BrandStatus.ACTIVATED.equals(status)) {
            if (!productRepositoryCustom.hasProductsInAllCategories(brand.getId())) {
                throw new IllegalArgumentException("모든 카테고리에 상품이 등록되어 있어야 합니다.");
            }
            productRepositoryCustom.updateProductStatusByBrandId(brand.getId(), ProductStatus.STANDBY, ProductStatus.ACTIVATED);
            List<Product> lowestProducts = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brand);
            // 카테고리별 가장 낮은 금액을 가진 상품을 조회하고 그 상품의 총합을 세팅하는 쿼리
            brand.setTotalLowestPrice(lowestProducts.stream()
                    .map(Product::getPrice)
                    .reduce(0L, Long::sum));

            // 카테고리별 최저 금액 상품을 갱신하는 쿼리
            lowestProducts.forEach(
                    product -> product.getCategory().checkLowestProduct(product)
            );
        } else if (BrandStatus.REMOVED.equals(status)) {
            productRepositoryCustom.removeProductsByBrandId(brand.getId());
            List<Product> lowestProducts = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brand);
            // 카테고리별 최저 금액 상품을 갱신하는 쿼리
            lowestProducts.forEach(
                    product -> {
                        if (!product.getCategory().getLowestProduct().equals(product)) {
                            return;
                        }
                        productRepository.findFirstByCategoryAndStatusOrderByPriceAsc(product.getCategory(), ProductStatus.ACTIVATED)
                                .ifPresent(lowestProduct ->
                                        product.getCategory().checkLowestProduct(lowestProduct)
                                );
                    }
            );
        }
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
