package com.example.spring_exam.category.command.mapper;

import com.example.spring_exam.category.command.domain.Category;
import com.example.spring_exam.category.command.dto.CreateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryCommandMapper {
    CategoryCommandMapper INSTANCE = Mappers.getMapper(CategoryCommandMapper.class);

    @Mapping(target = "slug", expression = "java(toSlug(request.name()))")
    @Mapping(target = "isLeaf", constant = "true")
    Category entityToReqDTO(CreateCategoryRequest request);

    default String toSlug(String name) {
        if (name == null) return null;
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}