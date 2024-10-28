package com.musinsa.task.coordination.domain.product.repository;

import com.musinsa.task.coordination.category.entity.QCategory;
import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.product.entity.QProduct;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustom {
    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryRepository categoryRepository;

    public List<Product> selectLowestPerCategoryProducts() {
        QCategory category = QCategory.category;
        return jpaQueryFactory
                .select(category.lowestProduct)
                .from(category)
                .fetch();
    }

    // 브랜드의 카테고리별 최저 상품을 구하는 쿼리
    public List<Product> selectLowestProductsByBrandGroupByCategory(Brand brand) {
        QProduct product = QProduct.product;
        return jpaQueryFactory.selectFrom(product)
                .where(product.id.in(
                        JPAExpressions.select(product.id)
                                .from(product)
                                .where(product.brand.eq(brand))
                                .groupBy(product.category)
                                .having(product.price.eq(product.price.min()))
                ))
                .fetch();
    }

    public long updateProductStatusByBrandId(Long id, ProductStatus before, ProductStatus after) {
        QProduct product = QProduct.product;
        return new JPAUpdateClause(entityManager, product)
                .where(product.brand.id.eq(id)
                        .and(product.status.eq(before)))
                .set(product.status, after)
                .execute();
    }

    public long removeProductsByBrandId(Long id) {
        QProduct product = QProduct.product;
        return jpaQueryFactory
                .update(product)
                .set(product.status, ProductStatus.REMOVED)
                .where(product.brand.id.eq(id))
                .execute();
    }

    public boolean hasProductsInAllCategories(Long brandId) {
        // 해당 브랜드의 상품을 카테고리별로 그룹화하여 존재여부 조건
        QProduct product = QProduct.product;
        long countOfCategoriesWithProducts = jpaQueryFactory
                .select(product.category.id, product.count())
                .from(product)
                .where(product.brand.id.eq(brandId)
                        .and(product.status.eq(ProductStatus.STANDBY)))
                .groupBy(product.category.id)
                .having(product.count().gt(0))
                .fetch()
                .size();

        // 전체 카테고리 수를 가져옴
        long totalCategoryCount = categoryRepository.count();

        // 브랜드에 해당하는 상품이 각 카테고리별로 최소 하나씩 있는지 확인
        return countOfCategoriesWithProducts == totalCategoryCount;
    }
}
