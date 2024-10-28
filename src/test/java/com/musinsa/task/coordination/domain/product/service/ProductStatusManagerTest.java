package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.product.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.BrandNotActiveException;
import com.musinsa.task.coordination.domain.product.exception.LastProductInBrandCategoryException;
import com.musinsa.task.coordination.domain.product.exception.ProductStateNotAllowedException;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductStatusManagerTest {

    @InjectMocks
    private ProductStatusManager productStatusManager;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LowestPriceChecker lowestPriceChecker;

    @Test
    void testUpdateProductStatus_updateToStandby() {
        Product product = Product.builder()
                .status(ProductStatus.STANDBY)
                .build();
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setStatus(ProductStatus.STANDBY);
        assertThrows(ProductStateNotAllowedException.class,
                () -> productStatusManager.updateProductStatus(product, updateDto));
    }

    @Test
    void testUpdateProductStatus_updateBeforeBrandActivate() {
        Product product = Product.builder()
                .status(ProductStatus.STANDBY)
                .brand(Brand.builder().status(BrandStatus.STANDBY).build())
                .build();
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setStatus(ProductStatus.ACTIVATED);
        assertThrows(BrandNotActiveException.class,
                () -> productStatusManager.updateProductStatus(product, updateDto));
    }

    @Test
    void testRemoveProduct_whenOnlyOneProductInBrandCategory() {
        Brand brand = Brand.builder()
                .build();
        Category category = Category.builder()
                .build();
        Product product = Product.builder()
                .brand(brand)
                .category(category)
                .status(ProductStatus.ACTIVATED)
                .build();

        when(productRepository.countByBrandAndCategory(brand, category)).thenReturn(1L);
        assertThrows(LastProductInBrandCategoryException.class, () -> productStatusManager.removeProduct(product));
    }

    @Test
    void testChangeStatus() {
        Product product = Product.builder()
                .status(ProductStatus.REMOVED)
                .brand(Brand.builder().status(BrandStatus.ACTIVATED).build())
                .category(Category.builder().build())
                .build();
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setStatus(ProductStatus.ACTIVATED);

        assertThrows(ProductStateNotAllowedException.class, () -> productStatusManager.updateProductStatus(product, updateDto));
    }
}