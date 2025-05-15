package com.example.spring_exam.image.utils;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.image.command.domain.ImageType;

import java.time.LocalDate;
import java.util.UUID;

public class ImageUtils {

    public static String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    public static String buildFilePath(ImageType type, String extension) {
        return type.getValue() + "/" + LocalDate.now() + "/" + UUID.randomUUID() + extension;
    }
}