package com.example.spring_exam.category.command.application;

import com.example.spring_exam.category.command.dto.CreateCategoryRequest;

public interface CategoryCommandService {
    Long createCategory(CreateCategoryRequest createCategoryRequest);
}
