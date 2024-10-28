package com.musinsa.task.coordination.domain.brand.repository;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus brandStatus);
}
