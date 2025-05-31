package com.example.spring_exam.category.query.mapper;

import com.example.spring_exam.category.query.domain.CategoryReadModel;
import com.example.spring_exam.category.query.dto.res.CategoryRes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryQueryMapper {
    CategoryQueryMapper INSTANCE = Mappers.getMapper(CategoryQueryMapper.class);

    CategoryRes entityToReqDTO(CategoryReadModel category);
}