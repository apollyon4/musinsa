package com.musinsa.task.coordination.domain.brand.service;

import com.musinsa.task.coordination.domain.brand.dto.req.BrandCreateDto;
import com.musinsa.task.coordination.domain.brand.dto.req.BrandUpdateDto;
import com.musinsa.task.coordination.domain.brand.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.BrandNotFoundException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BrandServiceIntegrationTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("브랜드 추가 성공 테스트")
    void addBrand_success() {
        BrandCreateDto brandCreateDto = new BrandCreateDto();
        brandCreateDto.setName("brand1");

        BrandResponseDto response = brandService.addBrand(brandCreateDto);

        assertThat(response.getName()).isEqualTo("brand1");
        assertEquals(response.getStatus(), BrandStatus.STANDBY.name());
    }

    @Test
    @DisplayName("브랜드 업데이트 성공 테스트")
    void updateBrand_success() {
        Brand brand = brandRepository.save(Brand.builder()
                .name("brand1")
                .status(BrandStatus.STANDBY)
                .build());

        BrandUpdateDto updateDto = new BrandUpdateDto();
        updateDto.setId(brand.getId());
        updateDto.setName("brand2");

        BrandResponseDto updatedBrand = brandService.updateBrand(updateDto);

        assertThat(updatedBrand.getName()).isEqualTo("brand2");
    }

    @Test
    @DisplayName("브랜드 삭제 성공 테스트")
    void deleteBrand_success() {
        Brand brand = brandRepository.save(Brand.builder()
                .name("brand1")
                .status(BrandStatus.ACTIVATED)
                .build());
        productRepository.save(Product.builder()
                .price(1L)
                .brand(brand)
                .category(categoryRepository.getReferenceById(1L))
                .status(ProductStatus.ACTIVATED)
                .build());

        brandService.deleteBrand(brand.getId());

        Brand deletedBrand = brandRepository.findById(brand.getId()).orElse(null);
        assertThat(deletedBrand).isNotNull();
        assertThat(deletedBrand.getStatus()).isEqualTo(BrandStatus.REMOVED);
    }

    @Test
    @DisplayName("브랜드 목록 조회 테스트")
    void getBrands_success() {
        brandRepository.save(Brand.builder().name("brand1").status(BrandStatus.ACTIVATED).build());
        brandRepository.save(Brand.builder().name("brand2").status(BrandStatus.STANDBY).build());
        brandRepository.save(Brand.builder().name("brand3").status(BrandStatus.REMOVED).build());

        List<BrandResponseDto> brands = brandService.getBrands();
        assertThat(brands).extracting("name").contains("brand1", "brand2");
    }

    @Test
    @DisplayName("브랜드 단건 조회 성공 테스트")
    void getBrand_success() {
        Brand brand = brandRepository.save(Brand.builder()
                .name("brand1")
                .status(BrandStatus.STANDBY)
                .build());

        BrandResponseDto response = brandService.getBrand(brand.getId());

        assertThat(response.getName()).isEqualTo("brand1");
        assertThat(response.getStatus()).isEqualTo(BrandStatus.STANDBY.name());
    }

    @Test
    @DisplayName("존재하지 않는 브랜드 조회 실패 테스트")
    void getBrand_notFound() {
        Long invalidId = 999L;

        assertThrows(BrandNotFoundException.class, () -> brandService.getBrand(invalidId));
    }
}