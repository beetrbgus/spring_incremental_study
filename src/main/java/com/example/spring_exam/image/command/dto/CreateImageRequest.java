package com.example.spring_exam.image.command.dto;

import com.example.spring_exam.image.command.domain.ImageType;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateImageRequest(
        @NotNull
        ImageType imageType,
        @NotNull(message = "썸네일 파일은 필수 입니다.")
        MultipartFile file,
        boolean isRepresentative
) {
}