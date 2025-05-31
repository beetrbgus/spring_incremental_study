
package com.example.spring_exam.category.query.application;

import com.example.spring_exam.category.query.domain.CategoryReadModel;
import com.example.spring_exam.category.query.dto.res.CategoryListRes;
import com.example.spring_exam.category.query.dto.res.CategoryRes;
import com.example.spring_exam.category.query.infrastructure.persistence.CategoryQueryRepository;
import com.example.spring_exam.category.query.mapper.CategoryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryQueryRepository categoryQueryRepository;

    public CategoryListRes getCategoryList() {
        List<CategoryReadModel> categoryReadModelList = categoryQueryRepository.getCategoryList();
        CategoryQueryMapper categoryQueryMapper = CategoryQueryMapper.INSTANCE;

        List<CategoryRes> categoryResList = categoryReadModelList.stream()
                .map(categoryQueryMapper::entityToReqDTO)
                .toList();

        return new CategoryListRes(categoryResList);
    }
}
