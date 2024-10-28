package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LowestPriceCheckerTest {

    @InjectMocks
    private LowestPriceChecker lowestPriceChecker;

    @Mock
    private ProductRepositoryCustom productRepositoryCustom;

    @Test
    void testUpdateLowestProducts_singleBrandLowestProductInCategory() {
        Category category = Category.builder()
                .id(1L)
                .build();
        Product product = Product.builder()
                .id(1L)
                .price(1000L)
                .brand(Brand.builder().build())
                .category(category)
                .status(ProductStatus.ACTIVATED)
                .build();
        category.checkLowestProduct(product);

        lowestPriceChecker.updateBrandLowestProducts(product);
        assertEquals(product, category.getLowestProduct());
    }

    @Test
    void testUpdateLowestProducts_calculatesTotalBrandLowestPriceForBrand() {
        Brand brand = Brand.builder()
                .id(1L)
                .name("brand1")
                .status(BrandStatus.ACTIVATED)
                .build();
        List<Product> products = List.of(
                Product.builder().id(1L).price(50L).brand(brand).build(),
                Product.builder().id(2L).price(150L).brand(brand).build()
        );

        when(productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brand))
                .thenReturn(products);

        lowestPriceChecker.updateBrandLowestProducts(Product.builder().id(1L).price(50L).brand(brand).build());
        assertEquals(200L, brand.getTotalLowestPrice());
    }
}