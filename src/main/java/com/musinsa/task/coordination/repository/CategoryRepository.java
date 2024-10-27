package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
