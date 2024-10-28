package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.product.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.BrandNotActiveException;
import com.musinsa.task.coordination.domain.product.exception.LastProductInBrandCategoryException;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStatusManager {

    private final ProductRepository productRepository;
    private final LowestPriceChecker lowestPriceChecker;

    public void updateProductStatus(Product product, ProductUpdateDto productUpdateDto) {
        if (productUpdateDto.getPrice() != null) {
            product.setPrice(productUpdateDto.getPrice());
        }

        if (productUpdateDto.getStatus() != null) {
            updateStatusAndConstraints(product, productUpdateDto.getStatus());
        }
    }

    public void removeProduct(Product product) {
        updateStatusAndConstraints(product, ProductStatus.REMOVED);
        productRepository.save(product);
    }

    private void updateStatusAndConstraints(Product product, ProductStatus status) {
        if (status == ProductStatus.ACTIVATED && product.getBrand().getStatus() != BrandStatus.ACTIVATED) {
            throw new BrandNotActiveException(product.getBrand().getId());
        }

        if (status == ProductStatus.REMOVED) {
            long count = productRepository.countByBrandAndCategory(product.getBrand(), product.getCategory());
            if (count <= 1) {
                throw new LastProductInBrandCategoryException(product.getBrand().getId(), product.getCategory().getId());
            }
        }

        product.changeStatus(status);
        lowestPriceChecker.updateLowestProducts(product);
    }
}