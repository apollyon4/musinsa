package com.musinsa.task.coordination.controller;

import com.musinsa.task.coordination.dto.req.BrandCreateDto;
import com.musinsa.task.coordination.dto.req.BrandUpdateDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBrand_createsNewBrand() {
        BrandCreateDto brandCreateDto = new BrandCreateDto();
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        when(brandService.addBrand(any(BrandCreateDto.class))).thenReturn(brandResponseDto);

        ResponseEntity<BrandResponseDto> response = brandController.addBrand(brandCreateDto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(brandResponseDto, response.getBody());
    }

    @Test
    void updateBrand_updatesExistingBrand() {
        BrandUpdateDto brandUpdateDto = new BrandUpdateDto();
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        when(brandService.updateBrand(any(BrandUpdateDto.class))).thenReturn(brandResponseDto);

        ResponseEntity<BrandResponseDto> response = brandController.updateBrand(1L, brandUpdateDto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(brandResponseDto, response.getBody());
    }

    @Test
    void deleteBrand_deletesExistingBrand() {
        doNothing().when(brandService).deleteBrand(anyLong());

        ResponseEntity<String> response = brandController.deleteBrand(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("success", response.getBody());
    }

    @Test
    void getBrand_returnsBrandDetails() {
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        when(brandService.getBrand(anyLong())).thenReturn(brandResponseDto);

        ResponseEntity<BrandResponseDto> response = brandController.getBrand(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(brandResponseDto, response.getBody());
    }

    @Test
    void getBrands_returnsListOfBrands() {
        List<BrandResponseDto> brandResponseDtos = Collections.singletonList(new BrandResponseDto());
        when(brandService.getBrands()).thenReturn(brandResponseDtos);

        ResponseEntity<List<BrandResponseDto>> response = brandController.getBrands();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(brandResponseDtos, response.getBody());
    }
}