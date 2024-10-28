package com.musinsa.task.coordination.domain.product.repository;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class ProductRepositoryCustomTest {

    @Autowired
    private EntityManager entityManager;

    private ProductRepositoryCustom productRepositoryCustom;
    private Brand[] brands;
    private Category[] categories;
    private Product[] products;

    @BeforeEach
    void setUp() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        productRepositoryCustom = new ProductRepositoryCustom(entityManager, jpaQueryFactory);

        // 사전 준비된 데이터 테이블 테스트 데이터 초기화
        brands = new Brand[]{
                Brand.builder().name("brand1").status(BrandStatus.ACTIVATED).build()
        };
        entityManager.persist(brands[0]);
        categories = new Category[]{
                Category.builder().name("category1").build(),
                Category.builder().name("category2").build()
        };
        entityManager.persist(categories[0]);
        entityManager.persist(categories[1]);

        products = new Product[]{
                Product.builder().price(150L).brand(brands[0]).category(categories[0]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().price(200L).brand(brands[0]).category(categories[1]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().price(300L).brand(brands[0]).category(categories[0]).status(ProductStatus.ACTIVATED).build(),
                Product.builder().price(400L).brand(brands[0]).category(categories[1]).status(ProductStatus.ACTIVATED).build()
        };
        entityManager.persist(products[0]);
        entityManager.persist(products[1]);
        entityManager.persist(products[2]);
        entityManager.persist(products[3]);
        entityManager.flush();

        categories[0].checkLowestProduct(products[0]);
        categories[0].checkLowestProduct(products[2]);
        categories[1].checkLowestProduct(products[1]);
        categories[1].checkLowestProduct(products[3]);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testSelectLowestPerCategoryProducts() {
        // Given: 각 카테고리별로 최저가 상품이 설정되어 있음

        // When
        List<Product> lowestProducts = productRepositoryCustom.selectLowestPerCategoryProducts();

        // Then
        assertThat(lowestProducts).hasSize(2);
        assertThat(lowestProducts).contains(products[0], products[1]);
    }

    @Test
    void testUpdateProductStatusByBrandId() {
        // Given: 브랜드의 상품 상태가 STANDBY
        Brand standbyBrand = Brand.builder().name("brand1").status(BrandStatus.STANDBY).build();
        Product product1 = Product.builder().price(150L).brand(standbyBrand).category(categories[0]).status(ProductStatus.STANDBY).build();
        Product product2 = Product.builder().price(200L).brand(standbyBrand).category(categories[1]).status(ProductStatus.STANDBY).build();
        entityManager.persist(standbyBrand);
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        // When
        long updatedCount = productRepositoryCustom.updateProductStatusByBrandId(standbyBrand.getId(), ProductStatus.STANDBY, ProductStatus.ACTIVATED);

        // Then
        assertThat(updatedCount).isEqualTo(2);
        entityManager.clear();

        // 각 상품 상태가 ACTIVATED로 변경되었는지 확인
        Product updatedProduct1 = entityManager.find(Product.class, product1.getId());
        Product updatedProduct2 = entityManager.find(Product.class, product2.getId());

        assertThat(updatedProduct1.getStatus()).isEqualTo(ProductStatus.ACTIVATED);
        assertThat(updatedProduct2.getStatus()).isEqualTo(ProductStatus.ACTIVATED);
    }

    @Test
    void testRemoveProductsByBrandId() {
        // When
        long removedCount = productRepositoryCustom.removeProductsByBrandId(brands[0].getId());

        // Then
        assertThat(removedCount).isEqualTo(4);
        entityManager.clear();

        // 각 상품 상태가 REMOVED로 변경되었는지 확인
        Product removedProduct1 = entityManager.find(Product.class, products[0].getId());
        Product removedProduct2 = entityManager.find(Product.class, products[1].getId());
        Product removedProduct3 = entityManager.find(Product.class, products[2].getId());
        Product removedProduct4 = entityManager.find(Product.class, products[3].getId());

        assertThat(removedProduct1.getStatus()).isEqualTo(ProductStatus.REMOVED);
        assertThat(removedProduct2.getStatus()).isEqualTo(ProductStatus.REMOVED);
        assertThat(removedProduct3.getStatus()).isEqualTo(ProductStatus.REMOVED);
        assertThat(removedProduct4.getStatus()).isEqualTo(ProductStatus.REMOVED);
    }

    @Test
    void testHasProductsInAllCategories_true() {
        Brand standbyBrand = Brand.builder().name("brand1").status(BrandStatus.STANDBY).build();
        Product product1 = Product.builder().price(150L).brand(standbyBrand).category(categories[0]).status(ProductStatus.STANDBY).build();
        // 같은 카테고리 상품이 2개
        Product product2 = Product.builder().price(200L).brand(standbyBrand).category(categories[1]).status(ProductStatus.STANDBY).build();
        Product product3 = Product.builder().price(300L).brand(standbyBrand).category(categories[1]).status(ProductStatus.STANDBY).build();

        entityManager.persist(standbyBrand);
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();

        // When
        long result = productRepositoryCustom.hasProductsInAllCategories(standbyBrand.getId());

        // Then
        assertThat(result).isEqualTo(2); // 모든 카테고리에서 상품이 있음
    }

    @Test
    void testHasProductsInAllCategories_false() {
        Brand standbyBrand = Brand.builder().name("brand1").status(BrandStatus.STANDBY).build();
        // 같은 카테고리 상품이 2개
        Product product2 = Product.builder().price(200L).brand(standbyBrand).category(categories[1]).status(ProductStatus.STANDBY).build();
        Product product3 = Product.builder().price(300L).brand(standbyBrand).category(categories[1]).status(ProductStatus.STANDBY).build();

        // 다른 카테고리에 상품 제거
        entityManager.persist(standbyBrand);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();

        // When
        long result = productRepositoryCustom.hasProductsInAllCategories(standbyBrand.getId());

        // Then
        assertThat(result).isEqualTo(1); // 모든 카테고리에 상품이 있지 않음
    }
}
