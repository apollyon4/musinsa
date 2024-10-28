package com.musinsa.task.coordination.domain.brand.repository;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus brandStatus);
}
