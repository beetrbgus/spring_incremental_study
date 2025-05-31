package com.example.spring_exam.category.presentation;

import com.example.spring_exam.category.command.application.CategoryCommandService;
import com.example.spring_exam.category.command.dto.CreateCategoryRequest;
import com.example.spring_exam.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/category/command")
@RequiredArgsConstructor
public class CategoryCommandController {
    private final CategoryCommandService categoryCommandService;

    @PostMapping
    public CommonResponse<Long> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Long categoryId = categoryCommandService.createCategory(createCategoryRequest);

        return CommonResponse.created(categoryId);
    }
}
