package com.example.spring_exam.image.command.infrastucture.uploader;

import com.example.spring_exam.image.command.domain.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String upload(MultipartFile file, ImageType imageType);
}
