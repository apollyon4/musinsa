package com.musinsa.task.coordination.controller;

import com.musinsa.task.coordination.dto.req.CreateProductDto;
import com.musinsa.task.coordination.dto.req.UpdateProductDto;
import com.musinsa.task.coordination.dto.res.CategoryLowestHighestProductResponseDto;
import com.musinsa.task.coordination.dto.res.LowestBrandProductsResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 구현1) 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
    @GetMapping("/lowest-per-category")
    public ResponseEntity<ProductListResponseDto> getLowestPerCategoryProducts() {
        return ResponseEntity.ok(productService.getLowestPerCategoryProducts());
    }

    // 구현2) 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    @GetMapping("/lowest-brand")
    public ResponseEntity<LowestBrandProductsResponseDto> getLowestBrandProducts() {
        return ResponseEntity.ok(productService.getLowestBrandProducts());
    }

    // 구현3) 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
    @GetMapping("/category/{categoryName}/lowest-highest")
    public ResponseEntity<CategoryLowestHighestProductResponseDto> getLowestHighestByCategoryProducts(@PathVariable("categoryName") String categoryName) {
        return ResponseEntity.ok(productService.getLowestHighestByCategoryProducts(categoryName));
    }

    // 구현4-2) 상품을 추가 / 업데이트 / 삭제하는 API
    @PostMapping("")
    public ResponseEntity<ProductResponseDto> addProduct(CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.addProduct(createProductDto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("productId") Long productId, UpdateProductDto updateProductDto) {
        updateProductDto.setId(productId);
        return ResponseEntity.ok(productService.updateProduct(updateProductDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("success");
    }
}
