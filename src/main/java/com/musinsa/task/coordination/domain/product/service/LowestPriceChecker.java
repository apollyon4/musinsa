package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LowestPriceChecker {

    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    /**
     * 변경된 상품의 브랜드 최저가를 갱신하는 메서드
     */
    public void updateBrandLowestProducts(Product product) {
        List<Product> products = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(product.getBrand());
        product.getBrand().setTotalLowestPrice(
                products.stream().map(Product::getPrice).reduce(0L, Long::sum)
        );
    }

    /**
     * 변경된 상품의 카테고리 최저가를 갱신하는 메서드
     */
    public void updateCategoryLowestProduct(Product product) {
        if (ProductStatus.REMOVED.equals(product.getCategory().getLowestProduct().getStatus()) ||
                ObjectUtils.notEqual(product.getCategory().getLowestProduct(), (product))) {
            productRepository.findFirstByCategoryAndStatusOrderByPriceAsc(product.getCategory(), ProductStatus.ACTIVATED)
                    .ifPresent(lowestProduct -> product.getCategory().checkLowestProduct(lowestProduct));
        }
    }
}
