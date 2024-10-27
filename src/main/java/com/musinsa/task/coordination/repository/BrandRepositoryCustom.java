package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.entity.QBrand;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // 최저 금액을 가진 브랜드를 구하는 쿼리
    public Brand selectLowestBrand() {
        QBrand brand = QBrand.brand;
        Brand lowestBrand = jpaQueryFactory
                .selectFrom(brand)
                .where(brand.status.eq(BrandStatus.ACTIVATED))
                .orderBy(brand.totalLowestPrice.asc())
                .fetchFirst();
        if (lowestBrand == null) {
            throw new IllegalArgumentException("활성화된 브랜드가 존재하지 않습니다.");
        }
        return lowestBrand;
    }
}
