package com.example.spring_exam.category.query.dto.res;

import java.util.List;

public record CategoryListRes (
    List<CategoryRes> categoryList
) {}
