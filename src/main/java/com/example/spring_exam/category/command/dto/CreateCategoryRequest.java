package com.example.spring_exam.category.command.dto;

import com.example.spring_exam.common.validator.annotation.NullablePositive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCategoryRequest(
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String name,

        @NotNull(message = "카테고리 깊이는 필수입니다.")
        @Min(value = 0, message = "카테고리 깊이는 0 이상이어야 합니다.")
        Integer depth,

        @NotNull(message = "정렬 순서는 필수입니다.")
        @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다.")
        Integer sortOrder,

        @Size(max = 500, message = "설명은 500자 이하로 입력해주세요.")
        String description,

        @NotNull(message = "표시 여부는 필수입니다.")
        Boolean isVisible,

        List<ImageRequest> imageList,

        @NullablePositive(message = "상위 카테고리 ID는 음수가 될 수 없습니다.")
        Long parentId
) {
}