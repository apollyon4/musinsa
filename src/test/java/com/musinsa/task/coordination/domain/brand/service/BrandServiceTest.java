package com.musinsa.task.coordination.domain.brand.service;

import com.musinsa.task.coordination.domain.brand.dto.req.BrandCreateDto;
import com.musinsa.task.coordination.domain.brand.dto.req.BrandUpdateDto;
import com.musinsa.task.coordination.domain.brand.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.BrandNotFoundException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @InjectMocks
    private BrandService brandService;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private BrandStatusManager brandStatusManager;

    @Test
    void addBrand_withValidData_returnsBrandResponse() {
        BrandCreateDto brandCreateDto = new BrandCreateDto();
        Brand brand = Brand.builder()
                .status(BrandStatus.STANDBY)
                .build();
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandResponseDto response = brandService.addBrand(brandCreateDto);

        assertNotNull(response);
    }

    @Test
    void updateBrand_withValidData_returnsUpdatedBrandResponse() {
        BrandUpdateDto brandUpdateDto = new BrandUpdateDto();
        brandUpdateDto.setId(1L);
        Brand brand = Brand.builder().id(1L).build();
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(brand)).thenReturn(brand);

        BrandResponseDto response = brandService.updateBrand(brandUpdateDto);

        assertNotNull(response);
    }

    @Test
    void deleteBrand_withValidId_changesBrandStatus() {
        Brand brand = Brand.builder().build();
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));

        brandService.deleteBrand(1L);
    }


    @Test
    void updateBrand_withNonExistingBrand_throwsException() {
        BrandUpdateDto brandUpdateDto = new BrandUpdateDto();
        brandUpdateDto.setId(1L);
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.updateBrand(brandUpdateDto));
    }

    @Test
    void deleteBrand_withNonExistingBrand_throwsException() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.deleteBrand(1L));
    }

    @Test
    void getBrand_withNonExistingBrand_throwsException() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.getBrand(1L));
    }
}