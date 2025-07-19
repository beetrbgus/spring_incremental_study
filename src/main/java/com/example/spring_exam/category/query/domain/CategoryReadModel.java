package com.example.spring_exam.category.query.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryReadModel {
    private Long id;
    private String name;
    private Integer depth;
    private Integer sortOrder;
    private Boolean isVisible;
    private Boolean isLeaf;
    private String slug;
    private String description;

    private Long parentId;

    private List<CategoryReadModel> children = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
