package com.musinsa.task.coordination.domain.brand.service;

import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class BrandServiceTest {
    @InjectMocks
    private BrandService brandService;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ProductRepositoryCustom productRepositoryCustom;
    @Mock
    private ProductRepository productRepository;

//    @Test
//    void addBrand_withValidData_returnsBrandResponse() {
//        BrandCreateDto brandCreateDto = new BrandCreateDto();
//        Brand brand = Brand.builder().build();
//        when(brandRepository.save(any(Brand.class))).thenReturn(brand);
//
//        BrandResponseDto response = brandService.addBrand(brandCreateDto);
//
//        assertNotNull(response);
//    }

//    @Test
//    void updateBrand_withValidData_returnsUpdatedBrandResponse() {
//        BrandUpdateDto brandUpdateDto = new BrandUpdateDto();
//        Brand brand = Brand.builder().build();
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));
//
//        BrandResponseDto response = brandService.updateBrand(brandUpdateDto);
//
//        assertNotNull(response);
//    }

//    @Test
//    void deleteBrand_withValidId_changesBrandStatus() {
//        Brand brand = Brand.builder().build();
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));
//
//        brandService.deleteBrand(1L);
//
//        verify(brandRepository, times(1)).save(brand);
//    }
//
//    @Test
//    void getBrand_withValidId_returnsBrandResponse() {
//        Brand brand = new Brand();
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));
//
//        BrandResponseDto response = brandService.getBrand(1L);
//
//        assertNotNull(response);
//    }
//
//    @Test
//    void getBrands_returnsBrandResponseList() {
//        List<Brand> brands = Collections.singletonList(new Brand());
//        when(brandRepository.findAll()).thenReturn(brands);
//
//        List<BrandResponseDto> response = brandService.getBrands();
//
//        assertEquals(brands.size(), response.size());
//    }

//    @Test
//    void addBrand_withNullName_throwsException() {
//        BrandCreateDto brandCreateDto = new BrandCreateDto();
//        brandCreateDto.setName(null);
//
//        assertThrows(IllegalArgumentException.class, () -> brandService.addBrand(brandCreateDto));
//    }
//
//    @Test
//    void updateBrand_withNonExistingBrand_throwsException() {
//        BrandUpdateDto brandUpdateDto = new BrandUpdateDto();
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(BrandNotFoundException.class, () -> brandService.updateBrand(brandUpdateDto));
//    }
//
//    @Test
//    void deleteBrand_withNonExistingBrand_throwsException() {
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(BrandNotFoundException.class, () -> brandService.deleteBrand(1L));
//    }
//
//    @Test
//    void getBrand_withNonExistingBrand_throwsException() {
//        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(BrandNotFoundException.class, () -> brandService.getBrand(1L));
//    }
}