package com.musinsa.task.coordination.repository;

import com.musinsa.task.coordination.dto.res.CategoryLowestHighestProductResponseDto;
import com.musinsa.task.coordination.dto.res.LowestBrandProductsResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.entity.*;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustom {
    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public long updateProductStatusByBrandId(Long id, ProductStatus before, ProductStatus after) {
        QProduct product = QProduct.product;
        return new JPAUpdateClause(entityManager, product)
                .where(product.brand.id.eq(id)
                        .and(product.status.eq(before)))
                .set(product.status, after)
                .execute();
    }

    public ProductListResponseDto selectLowestPerCategoryProducts() {
        QCategory category = QCategory.category;

        List<Product> products = jpaQueryFactory
                .select(category.lowestProduct) // lowestProduct를 직접 선택
                .from(category)
                .fetch();
        return new ProductListResponseDto(products.stream()
                .map(product -> ProductResponseDto.builder()
                        .price(product.getPrice())
                        .categoryName(product.getCategory().getName())
                        .brandName(product.getBrand().getName())
                        .build())
                .collect(Collectors.toList()));
    }

    /*
    브랜드에는 n개의 상품(각 카테고리마다 하나씩은 보장)이 존재

    각 카테고리별로 상품을 등록하기 전까지는 브랜드의 최저 금액을 계산 및 반환해선 안됌
    각 카테고리별로 상품을 등록했다 하더라도 이후에 상품을 삭제할 가능성이 있음
      1. 카테고리 별로 상품을 등록했는지 확인 필요 -> 상품을 다 등록(+standby)하지 않으면 브랜드가 활성화가 되지 않음
      2. 상품 삭제 시 카테고리에 다른 상품이 남았는지에 따라 추가 수행이 필요
    => 최저 금액 브랜드의 조회를 단순하게 만들기 위해 필요한 조건임..

    최저 금액을 가진 브랜드를 구하고, 그 브랜드에서 가장 낮은 금액을 가진 상품을 각 카테고리별로 구하는 쿼리가 필요함
     */
    public LowestBrandProductsResponseDto selectLowestBrandProducts() {
        // select brand where total is min & brand status is activated
        QBrand brand = QBrand.brand;
        Brand lowestBrand = jpaQueryFactory
                .select(brand)
                .orderBy(brand.totalLowestPrice.asc())
                .fetchFirst();

        // get brand items each category lowest product
        QProduct product = QProduct.product;
        List<Product> products = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.brand.eq(lowestBrand))
                .groupBy(product.category)
                .having(product.price.eq(
                        JPAExpressions
                                .select(product.price.min())
                                .from(product)
                ))
                .fetch();
        return LowestBrandProductsResponseDto.builder()
                .brandName(lowestBrand.getName())
                .products(products.stream()
                        .map(item -> ProductResponseDto.builder()
                                .price(item.getPrice())
                                .categoryName(item.getCategory().getName())
                                .build())
                        .collect(Collectors.toList()))
                .totalPrice(products.stream()
                        .reduce(0L, (sum, p) -> sum + p.getPrice(), Long::sum))
                .build();
    }

    public CategoryLowestHighestProductResponseDto selectLowestHighestByCategoryProducts(String categoryName) {
        QCategory category = QCategory.category;
        // TODO : null check
        Category targetCategory = jpaQueryFactory
                .selectFrom(category)
                .where(category.name.eq(categoryName))
                .fetchOne();

        // TODO : 쿼리 개선
        QProduct product = QProduct.product;
        Product lowestProduct = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.category.eq(targetCategory))
                .orderBy(product.price.asc())
                .fetchFirst();

        Product highestProduct = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.category.eq(targetCategory))
                .orderBy(product.price.desc())
                .fetchFirst();

        return CategoryLowestHighestProductResponseDto.builder()
                .categoryName(targetCategory.getName())
                .lowestProduct(ProductResponseDto.builder()
                        .price(lowestProduct.getPrice())
                        .brandName(lowestProduct.getBrand().getName())
                        .build()
                )
                .highestProduct(ProductResponseDto.builder()
                        .price(highestProduct.getPrice())
                        .brandName(highestProduct.getBrand().getName())
                        .build()
                )
                .build();
    }

    public boolean hasProductsInAllCategories(Long brandId) {
        // 해당 브랜드의 상품이 있는 카테고리 수
        // 해당 브랜드의 상품을 카테고리별로 그룹화하고 카운트
        QProduct product = QProduct.product;
        long countOfCategoriesWithProducts = jpaQueryFactory
                .select(product.category.id, product.count())
                .from(product)
                .where(product.brand.id.eq(brandId)
                        .and(product.status.in(ProductStatus.BLOCKED, ProductStatus.STANDBY)))
                .groupBy(product.category.id)
                .having(product.count().gt(0))
                .fetch()
                .size();

        // 전체 카테고리 수를 가져옴
        long totalCategoryCount = jpaQueryFactory
                .select(QCategory.category.count())
                .from(QCategory.category)
                .fetchOne();

        // 브랜드에 해당하는 상품이 각 카테고리별로 최소 하나씩 있는지 확인
        return countOfCategoriesWithProducts == totalCategoryCount;
    }

    public long removeProductsByBrandId(Long id) {
        QProduct product = QProduct.product;
        return jpaQueryFactory
                .update(product)
                .set(product.status, ProductStatus.REMOVED)
                .where(product.brand.id.eq(id))
                .execute();
    }
}
