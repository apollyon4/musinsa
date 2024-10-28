package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.MissingCategoryProductException;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import com.musinsa.task.coordination.domain.product.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.BrandNotActiveException;
import com.musinsa.task.coordination.domain.product.exception.LastProductInBrandCategoryException;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 상품 상태 변경 및 제약 조건을 관리하는 클래스
 * 상품 상태 전이 : STANDBY -> ACTIVATED -> REMOVED
 * STANDBY : 상품 생성 초기값
 * - 전이 조건 : 브랜드 활성화된 경우
 * ACTIVATED : 브랜드 활성화 시 수행됨
 * - 전이 조건 : 브랜드 내에 삭제하려는 상품이 둘 이상 남아 있을 경우
 */
@Component
@RequiredArgsConstructor
public class ProductStatusManager {
    private final LowestPriceChecker lowestPriceChecker;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    public void updateProductStatus(Product product, ProductUpdateDto productUpdateDto) {
        if (productUpdateDto.getPrice() != null) {
            product.setPrice(productUpdateDto.getPrice());
        }

        if (productUpdateDto.getStatus() != null) {
            updateStatusAndConstraints(product, productUpdateDto.getStatus());
        }
    }

    public void removeProduct(Product product) {
        updateStatusAndConstraints(product, ProductStatus.REMOVED);
    }

    /**
     * 상품 상태 변경 및 제약 조건을 업데이트
     */
    private void updateStatusAndConstraints(Product product, ProductStatus status) {
        if (status == ProductStatus.ACTIVATED && product.getBrand().getStatus() != BrandStatus.ACTIVATED) {
            throw new BrandNotActiveException(product.getBrand().getId());
        }

        if (status == ProductStatus.REMOVED) {
            long count = productRepository.countByBrandAndCategory(product.getBrand(), product.getCategory());
            if (count <= 1) {
                throw new LastProductInBrandCategoryException(product.getBrand().getId(), product.getCategory().getId());
            }
        }

        product.changeStatus(status);
        lowestPriceChecker.updateBrandLowestProducts(product);
        lowestPriceChecker.updateCategoryLowestProduct(product);
    }

    /**
     * 브랜드 활성화 후 브랜드 내의 상품들로 조회용 데이터를 갱신
     *
     * @param brand
     */
    public void updateConstraintAfterActivateBrand(Brand brand) {
        // 전체 카테고리 수를 가져옴
        long totalCategoryCount = categoryRepository.count();
        // 브랜드에 해당하는 상품이 각 카테고리별로 최소 하나씩 있는지 확인
        long categoryBrandInBrandCount = productRepositoryCustom.hasProductsInAllCategories(brand.getId());

        if (categoryBrandInBrandCount != totalCategoryCount) {
            throw new MissingCategoryProductException(brand.getId());
        }
        productRepositoryCustom.updateProductStatusByBrandId(brand.getId(), ProductStatus.STANDBY, ProductStatus.ACTIVATED);
        List<Product> lowestProducts = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brand);
        // 카테고리별 가장 낮은 금액을 가진 상품을 조회하고 그 상품의 총합을 세팅하는 쿼리
        brand.setTotalLowestPrice(lowestProducts.stream()
                .map(Product::getPrice)
                .reduce(0L, Long::sum));

        // 카테고리별 최저 금액 상품을 갱신하는 쿼리
        lowestProducts.forEach(product -> product.getCategory().checkLowestProduct(product));
    }

    /**
     * 상품 제거 후, 제거된 상품이 최저 금액인지 여부를 갱신
     *
     * @param brand
     */
    public void updateConstraintAfterRemoveBrand(Brand brand) {
        productRepositoryCustom.removeProductsByBrandId(brand.getId());
        List<Product> lowestProducts = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(brand);
        // 카테고리별 최저 금액 상품을 갱신하는 쿼리
        lowestProducts.forEach(lowestPriceChecker::updateCategoryLowestProduct);
    }
}