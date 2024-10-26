package com.musinsa.task.coordination.controller;

import com.musinsa.task.coordination.dto.req.CreateBrandDto;
import com.musinsa.task.coordination.dto.req.UpdateBrandDto;
import com.musinsa.task.coordination.dto.res.BrandResponseDto;
import com.musinsa.task.coordination.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // 구현4-1) 브랜드를 추가 / 업데이트 / 삭제하는 API
    @PostMapping("")
    public ResponseEntity<BrandResponseDto> addBrand(@RequestBody CreateBrandDto createBrandDto) {
        return ResponseEntity.ok(brandService.addBrand(createBrandDto));
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<BrandResponseDto> updateBrand(@PathVariable("brandId") Long brandId, @RequestBody UpdateBrandDto updateBrandDto) {
        updateBrandDto.setId(brandId);
        return ResponseEntity.ok(brandService.updateBrand(updateBrandDto));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable("brandId") Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("success");
    }
}
