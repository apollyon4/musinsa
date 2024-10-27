package com.musinsa.task.coordination.controller;

import com.musinsa.task.coordination.dto.req.ProductCreateDto;
import com.musinsa.task.coordination.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.dto.res.CategoryPriceRangeResponseDto;
import com.musinsa.task.coordination.dto.res.LowestPriceStyleResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.musinsa.task.coordination.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "구현1) 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API")
    @GetMapping("/lowest-per-category")
    public ResponseEntity<ProductListResponseDto> getLowestPerCategoryProducts() {
        return ResponseEntity.ok(productService.getLowestPerCategoryProducts());
    }

    @Operation(summary = "구현2) 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API")
    @GetMapping("/lowest-brand")
    public ResponseEntity<LowestPriceStyleResponseDto> getLowestBrandProducts() {
        return ResponseEntity.ok(productService.getLowestBrandProducts());
    }

    @Operation(summary = "구현3) 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API")
    @GetMapping("/category/{categoryName}/lowest-highest")
    public ResponseEntity<CategoryPriceRangeResponseDto> getLowestHighestByCategoryProducts(@PathVariable("categoryName") String categoryName) {
        return ResponseEntity.ok(productService.getLowestHighestByCategoryProducts(categoryName));
    }

    @Operation(summary = "구현4) 상품을 추가하는 API")
    @PostMapping("")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        return ResponseEntity.ok(productService.addProduct(productCreateDto));
    }

    @Operation(summary = "구현4) 상품을 업데이트하는 API")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductUpdateDto productUpdateDto) {
        productUpdateDto.setId(productId);
        return ResponseEntity.ok(productService.updateProduct(productUpdateDto));
    }

    @Operation(summary = "구현4) 상품을 삭제하는 API")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "Test) 상품 상태 변경 API")
    @PutMapping("/{productId}/status")
    public ResponseEntity<ProductResponseDto> changeProductStatus(@PathVariable("productId") Long productId, @RequestBody ProductStatus status) {
        return ResponseEntity.ok(productService.changeProductStatus(productId, status));
    }

    @Operation(summary = "Test) 상품 조회 API")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Operation(summary = "Test) 상품 목록 조회 API")
    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }
}
