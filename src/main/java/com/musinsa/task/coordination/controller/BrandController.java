package com.musinsa.task.coordination.controller;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.service.BrandService;
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
    public ResponseEntity<BrandResponseDto> addBrand(@RequestBody CreateBrandDto createBrandDto) {
        return ResponseEntity.ok(brandService.addBrand(createBrandDto));
    }

    @Operation(summary = "구현4) 브랜드 업데이트 API")
    @PutMapping("/{brandId}")
    public ResponseEntity<BrandResponseDto> updateBrand(@PathVariable("brandId") Long brandId, @RequestBody UpdateBrandDto updateBrandDto) {
        updateBrandDto.setId(brandId);
        return ResponseEntity.ok(brandService.updateBrand(updateBrandDto));
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
