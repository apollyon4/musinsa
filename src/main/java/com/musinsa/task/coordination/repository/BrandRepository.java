package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.enums.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus brandStatus);
}
