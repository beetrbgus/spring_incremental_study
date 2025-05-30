package com.example.spring_exam.category.query.presentation;

import com.example.spring_exam.category.query.application.CategoryQueryService;
import com.example.spring_exam.category.query.dto.res.CategoryListRes;
import com.example.spring_exam.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryQueryController {
    private final CategoryQueryService categoryQueryService;

    @PostMapping
    public CommonResponse<CategoryListRes> getCategoryList() {
        CategoryListRes categoryListRes = categoryQueryService.getCategoryList();

        return CommonResponse.ok(categoryListRes);
    }
}
