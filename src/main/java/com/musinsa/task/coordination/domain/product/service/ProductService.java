package com.musinsa.task.coordination.domain.product.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.NoBrandRecordsFoundException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import com.musinsa.task.coordination.domain.product.dto.req.ProductCreateDto;
import com.musinsa.task.coordination.domain.product.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.domain.product.dto.res.CategoryPriceRangeResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.LowestPriceStyleResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.domain.product.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.CategoryHasNoProductsException;
import com.musinsa.task.coordination.domain.product.exception.ProductNotFoundException;
import com.musinsa.task.coordination.domain.product.repository.ProductRepository;
import com.musinsa.task.coordination.domain.product.repository.ProductRepositoryCustom;
import com.musinsa.task.coordination.error.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductStatusManager productStatusManager;

    @Transactional(readOnly = true)
    public ProductListResponseDto getLowestPerCategoryProducts() {
        List<Product> products = productRepositoryCustom.selectLowestPerCategoryProducts();
        return new ProductListResponseDto(products);
    }

    @Transactional(readOnly = true)
    public LowestPriceStyleResponseDto getLowestBrandProducts() {
        Brand lowestBrand = brandRepository.findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus.ACTIVATED)
                .orElseThrow(() -> new NoBrandRecordsFoundException("No brand records found"));
        List<Product> products = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(lowestBrand);

        ProductListResponseDto productListResponseDto = new ProductListResponseDto(products);
        productListResponseDto.setBrandName(lowestBrand.getName());
        return new LowestPriceStyleResponseDto(productListResponseDto);
    }

    @Transactional(readOnly = true)
    public CategoryPriceRangeResponseDto getLowestHighestByCategoryProducts(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));
        Product lowestProduct = category.getLowestProduct();
        Product highestProduct = productRepository.findFirstByCategoryAndStatusOrderByPriceDesc(category, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new CategoryHasNoProductsException(categoryName));

        return new CategoryPriceRangeResponseDto(category.getName(), lowestProduct, highestProduct);
    }

    public ProductResponseDto addProduct(ProductCreateDto productCreateDto) {
        Product product = productRepository.save(Product.builder()
                .price(productCreateDto.getPrice())
                .category(categoryRepository.getReferenceById(productCreateDto.getCategoryId()))
                .brand(brandRepository.getReferenceById(productCreateDto.getBrandId()))
                .status(ProductStatus.STANDBY)
                .build());
        return ProductResponseDto.from(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findById(productUpdateDto.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDto.getId()));

        productStatusManager.updateProductStatus(product, productUpdateDto);

        return ProductResponseDto.from(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productStatusManager.removeProduct(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponseDto.builder()
                        .id(product.getId())
                        .price(product.getPrice())
                        .brandName(product.getBrand().getName())
                        .categoryName(product.getCategory().getName())
                        .status(product.getStatus())
                        .build())
                .toList();
    }
}
