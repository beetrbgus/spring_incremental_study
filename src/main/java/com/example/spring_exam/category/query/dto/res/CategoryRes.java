package com.example.spring_exam.category.query.dto.res;

import com.example.spring_exam.category.command.domain.Category;

import java.util.Comparator;
import java.util.List;

public record CategoryRes(
        Long id,
        String name,
        Integer depth,
        Integer sortOrder,
        Boolean isVisible,
        String description,
        List<CategoryRes> children
) {
    public static CategoryRes from(Category category) {
        return new CategoryRes(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getSortOrder(),
                category.getIsVisible(),
                category.getDescription(),
                category.getChildren() != null
                        ? category.getChildren().stream()
                        .sorted(Comparator.comparing(Category::getSortOrder))
                        .map(CategoryRes::from)
                        .toList()
                        : List.of()
        );
    }
}