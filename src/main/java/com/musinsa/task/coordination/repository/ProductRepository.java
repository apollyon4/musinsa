package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.entity.Category;
import com.musinsa.task.coordination.entity.Product;
import com.musinsa.task.coordination.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByBrandAndCategory(Brand brand, Category category);

    Optional<Product> findFirstByCategoryAndStatusOrderByPriceAsc(Category category, ProductStatus status);

    Optional<Product> findFirstByCategoryAndStatusOrderByPriceDesc(Category category, ProductStatus status);
}
