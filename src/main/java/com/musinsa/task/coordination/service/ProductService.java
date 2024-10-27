package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateProductDto;
import com.musinsa.task.coordination.dto.req.UpdateProductDto;
import com.musinsa.task.coordination.dto.res.CategoryLowestHighestProductResponseDto;
import com.musinsa.task.coordination.dto.res.LowestBrandProductsResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.entity.Product;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.CategoryRepository;
import com.musinsa.task.coordination.repository.ProductRepository;
import com.musinsa.task.coordination.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductListResponseDto getLowestPerCategoryProducts() {
        return productRepositoryCustom.selectLowestPerCategoryProducts();
    }

    public LowestBrandProductsResponseDto getLowestBrandProducts() {
        return productRepositoryCustom.selectLowestBrandProducts();
    }

    public CategoryLowestHighestProductResponseDto getLowestHighestByCategoryProducts(String categoryName) {
        return productRepositoryCustom.selectLowestHighestByCategoryProducts(categoryName);
    }

    public ProductResponseDto addProduct(CreateProductDto createProductDto) {
        Product product = productRepository.save(Product.builder()
                .price(createProductDto.getPrice())
                .category(categoryRepository.getReferenceById(createProductDto.getCategoryId()))
                .brand(brandRepository.getReferenceById(createProductDto.getBrandId()))
                .build());
        product = productRepository.save(product);
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public ProductResponseDto updateProduct(UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(updateProductDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        product.setPrice(updateProductDto.getPrice());
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        product.setStatus(ProductStatus.REMOVED);
        productRepository.save(product);
    }
}
