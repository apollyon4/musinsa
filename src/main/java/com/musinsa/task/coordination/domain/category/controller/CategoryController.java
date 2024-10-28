package com.musinsa.task.coordination.domain.category.controller;

import com.musinsa.task.coordination.domain.category.dto.res.CategoryResponseDto;
import com.musinsa.task.coordination.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDto>> getCategory() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
}
