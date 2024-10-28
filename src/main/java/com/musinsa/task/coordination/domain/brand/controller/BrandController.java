package com.musinsa.task.coordination.domain.brand.controller;

import com.musinsa.task.coordination.domain.brand.dto.req.BrandCreateDto;
import com.musinsa.task.coordination.domain.brand.dto.req.BrandUpdateDto;
import com.musinsa.task.coordination.domain.brand.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.domain.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @Operation(summary = "구현4) 브랜드 추가 API")
    @PostMapping("")
    public ResponseEntity<BrandResponseDto> addBrand(@RequestBody BrandCreateDto brandCreateDto) {
        return ResponseEntity.ok(brandService.addBrand(brandCreateDto));
    }

    @Operation(summary = "구현4) 브랜드 업데이트 API")
    @PutMapping("/{brandId}")
    public ResponseEntity<BrandResponseDto> updateBrand(@PathVariable("brandId") Long brandId, @RequestBody BrandUpdateDto brandUpdateDto) {
        brandUpdateDto.setId(brandId);
        return ResponseEntity.ok(brandService.updateBrand(brandUpdateDto));
    }

    @Operation(summary = "구현4) 브랜드 삭제 API")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable("brandId") Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "Test) 브랜드 조회 API")
    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponseDto> getBrand(@PathVariable("brandId") Long brandId) {
        return ResponseEntity.ok(brandService.getBrand(brandId));
    }

    @Operation(summary = "Test) 브랜드 목록 조회 API")
    @GetMapping("")
    public ResponseEntity<List<BrandResponseDto>> getBrands() {
        return ResponseEntity.ok(brandService.getBrands());
    }
}
