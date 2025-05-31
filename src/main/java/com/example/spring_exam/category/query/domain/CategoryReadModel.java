package com.example.spring_exam.category.query.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @QueryProjection
    public CategoryReadModel(Long id, String name, Integer depth, Integer sortOrder, Boolean isVisible, Boolean isLeaf, String slug, String description, Long parentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.depth = depth;
        this.sortOrder = sortOrder;
        this.isVisible = isVisible;
        this.isLeaf = isLeaf;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
