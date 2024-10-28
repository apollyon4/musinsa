package com.musinsa.task.coordination.domain.category.service;

import com.musinsa.task.coordination.domain.category.dto.res.CategoryResponseDto;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .map(CategoryResponseDto::new)
                .toList();
    }
}
