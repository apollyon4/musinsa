package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
