package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.NoBrandRecordsFoundException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import com.musinsa.task.coordination.domain.product.dto.req.ProductCreateDto;
import com.musinsa.task.coordination.domain.product.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.domain.product.dto.res.CategoryPriceRangeResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.LowestPriceStyleResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.ProductNotFoundException;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import com.musinsa.task.coordination.error.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductRepositoryCustom productRepositoryCustom;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ProductStatusManager productStatusManager;

    Category[] categories;

    Brand[] brands;

    Product[] products;

    @BeforeEach
    void setUp() {
        categories = new Category[]{
                Category.builder().id(1L).name("category1").build(),
                Category.builder().id(2L).name("category2").build()
        };
        brands = new Brand[]{
                Brand.builder().id(1L).name("brand1").status(BrandStatus.ACTIVATED).build(),
                Brand.builder().id(2L).name("brand2").status(BrandStatus.ACTIVATED).build()
        };
        products = new Product[]{
                Product.builder().id(1L).price(1000L).category(categories[0]).brand(brands[0]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().id(2L).price(2000L).category(categories[1]).brand(brands[0]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().id(3L).price(3000L).category(categories[0]).brand(brands[1]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().id(4L).price(4000L).category(categories[1]).brand(brands[1]).status(ProductStatus.ACTIVATED).build()
        };
        
        categories[0].checkLowestProduct(products[0]);
        categories[1].checkLowestProduct(products[1]);
    }

    @Test
    void testGetLowestPerCategoryProducts_success() {
        when(productRepositoryCustom.selectLowestPerCategoryProducts())
                .thenReturn(List.of(products));
        ProductListResponseDto response = productService.getLowestPerCategoryProducts();
        assertEquals(4, response.getProducts().size());
    }

    @Test
    void testGetLowestBrandProducts_success() {
        when(brandRepository.findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus.ACTIVATED))
                .thenReturn(Optional.ofNullable(brands[0]));
        when(productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brands[0]))
                .thenReturn(List.of(products[0], products[1]));
        LowestPriceStyleResponseDto responseDto = productService.getLowestBrandProducts();
        assertNotNull(responseDto);
        assertNotNull(responseDto.getProductListResponseDto());
        ProductListResponseDto products = responseDto.getProductListResponseDto();
        assertEquals(products.getBrandName(), brands[0].getName());
        assertEquals(products.getProducts().size(), 2);
        assertEquals(products.getTotalPrice(), "3,000");
    }

    @Test
    void testGetLowestBrandProducts_noBrand() {
        when(brandRepository.findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus.ACTIVATED))
                .thenReturn(Optional.empty());
        assertThrows(NoBrandRecordsFoundException.class, () -> productService.getLowestBrandProducts());
    }

    @Test
    void getLowestHighestByCategoryProducts() {
        when(categoryRepository.findByName("category1"))
                .thenReturn(Optional.ofNullable(categories[0]));
        when(productRepository.findFirstByCategoryAndStatusOrderByPriceDesc(categories[0], ProductStatus.ACTIVATED))
                .thenReturn(Optional.ofNullable(products[2]));
        CategoryPriceRangeResponseDto response = productService.getLowestHighestByCategoryProducts("category1");
        assertNotNull(response);
        assertEquals(response.getCategoryName(), "category1");
        assertNotNull(response.getLowestProduct());
        assertNotNull(response.getHighestProduct());
    }

    @Test
    void getLowestHighestByCategoryProducts_noCategory() {
        when(categoryRepository.findByName("category3"))
                .thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> productService.getLowestHighestByCategoryProducts("category3"));
    }

    @Test
    void testAddProduct() {
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setPrice(1000L);
        productCreateDto.setCategoryId(1L);
        productCreateDto.setBrandId(1L);

        when(categoryRepository.getReferenceById(1L)).thenReturn(categories[0]);
        when(brandRepository.getReferenceById(1L)).thenReturn(brands[0]);
        when(productRepository.save(any(Product.class))).thenReturn(products[0]);

        ProductResponseDto response = productService.addProduct(productCreateDto);

        assertNotNull(response);
        assertEquals("1,000", response.getPrice());
        assertEquals("category1", response.getCategoryName());
        assertEquals("brand1", response.getBrandName());
    }

    @Test
    void testAddProduct_categoryNotFound() {
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setPrice(1000L);
        productCreateDto.setCategoryId(3L);
        productCreateDto.setBrandId(1L);

        when(categoryRepository.getReferenceById(3L)).thenThrow(new CategoryNotFoundException("Category not found"));

        assertThrows(CategoryNotFoundException.class, () -> productService.addProduct(productCreateDto));
    }

    @Test
    void testAddProduct_brandNotFound() {
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setPrice(1000L);
        productCreateDto.setCategoryId(1L);
        productCreateDto.setBrandId(3L);

        when(categoryRepository.getReferenceById(1L)).thenReturn(categories[0]);
        when(brandRepository.getReferenceById(3L)).thenThrow(new NoBrandRecordsFoundException("Brand not found"));

        assertThrows(NoBrandRecordsFoundException.class, () -> productService.addProduct(productCreateDto));
    }

    @Test
    void testUpdateProduct() {
        ProductUpdateDto productUpdateDto = new ProductUpdateDto();
        productUpdateDto.setId(1L);
        productUpdateDto.setPrice(1500L);

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products[0]));

        ProductResponseDto response = productService.updateProduct(productUpdateDto);

        assertNotNull(response);
    }

    @Test
    void testUpdateProduct_productNotFound() {
        ProductUpdateDto productUpdateDto = new ProductUpdateDto();
        productUpdateDto.setId(100L);
        productUpdateDto.setPrice(1500L);

        when(productRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productUpdateDto));
    }

    @Test
    void testDeleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(products[0]));
        doNothing().when(productStatusManager).removeProduct(products[0]);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }

    @Test
    void testDeleteProduct_productNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(100L));
    }
}