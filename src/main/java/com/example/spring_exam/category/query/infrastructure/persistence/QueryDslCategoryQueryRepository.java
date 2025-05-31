package com.example.spring_exam.category.query.infrastructure.persistence;

import com.example.spring_exam.category.query.domain.CategoryReadModel;

import java.util.List;

public interface QueryDslCategoryQueryRepository {

    List<CategoryReadModel> getCategoryList();
}
