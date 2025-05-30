package com.example.spring_exam.category.command.application;

import com.example.spring_exam.category.command.domain.Category;
import com.example.spring_exam.category.command.dto.CreateCategoryRequest;
import com.example.spring_exam.category.command.dto.ImageRequest;
import com.example.spring_exam.category.command.infrastucture.persistence.CategoryCommandRepository;
import com.example.spring_exam.category.command.mapper.CategoryCommandMapper;
import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.exception.global.BadRequestException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.image.command.application.ImageCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryCommandRepository categoryCommandRepository;
    private final ImageCommandService imageCommandService;

    @Override
    public Long createCategory(CreateCategoryRequest createCategoryRequest) {
        try {
            Category category = CategoryCommandMapper.INSTANCE.entityToReqDTO(createCategoryRequest);

            if(createCategoryRequest.parentId() != null) {
                category.setParent(
                    categoryCommandRepository
                        .findById(createCategoryRequest.parentId())
                        .orElseThrow(BadRequestException::new)
                );
            }

            if (categoryCommandRepository.existsByName(createCategoryRequest.name())) {
                throw new BadRequestException(ErrorCode.ALREADY_EXISTS_NAME);
            }

            categoryCommandRepository.save(category);

            return category.getId();
        } catch (Exception e) {
            log.error("카테고리 업로드 실패. 원인: {}", createCategoryRequest.name(), e);
            ImageRequest imageRequest = createCategoryRequest.image();
            if (imageRequest != null) {
                try {
                    imageCommandService.deleteImage(imageRequest);
                } catch (Exception deleteEx) {
                    log.error("이미지 보상 삭제 실패! imageId: {}, 원인: {}", imageRequest.imageId(), deleteEx.getMessage(), deleteEx);
                }
            }
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
