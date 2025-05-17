package com.example.spring_exam.image.command.infrastucture.uploader;

import com.example.spring_exam.image.command.domain.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageCloudStorageService {
    String upload(MultipartFile file, ImageType imageType);

    void deleteImage(List<String> imageUrlList);
}
