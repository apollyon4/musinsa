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

import java.util.List;

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
                .status(ProductStatus.STANDBY)
                .build());
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
        // TODO : 제거된 상품은 처리할 수 없음.
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
        // TODO : 상품을 내릴때는, 브랜드&카테고리에 최소 하나의 상품이 활성화되어 있어야 함.
        product.setStatus(ProductStatus.REMOVED);
        productRepository.save(product);
    }

    public ProductResponseDto changeProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        if (ProductStatus.REMOVED.equals(product.getStatus())) {
            throw new IllegalArgumentException("제거된 상품은 되돌릴 수 없습니다.");
        }
        /*
            TODO : 상품을 활성화할때는, 브랜드가 활성화 되어있어야 함.
                   제거된 상품은 되돌릴 수 없음.
         */
        product.setStatus(status);
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

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
