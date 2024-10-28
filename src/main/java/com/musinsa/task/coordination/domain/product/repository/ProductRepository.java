package com.musinsa.task.coordination.domain.product.repository;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByBrandAndCategory(Brand brand, Category category);

    Optional<Product> findFirstByCategoryAndStatusOrderByPriceAsc(Category category, ProductStatus status);

    Optional<Product> findFirstByCategoryAndStatusOrderByPriceDesc(Category category, ProductStatus status);
}
