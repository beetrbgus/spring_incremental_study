package com.example.spring_exam.image.command.application;

import com.example.spring_exam.image.command.domain.Image;
import com.example.spring_exam.image.command.dto.CreateImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageResponse;
import com.example.spring_exam.image.command.infrastucture.persistence.ImageRepository;
import com.example.spring_exam.image.command.infrastucture.uploader.ImageUploader;
import com.example.spring_exam.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ImageCommandServiceImpl implements ImageCommandService {
    private final ImageRepository imageRepository;
    private final ImageUploader imageUploader;

    @Override
    public CreateImageResponse uploadImage(CreateImageRequest createImageRequest, User user) {
        try {
            String uploadedUrl = imageUploader.upload(createImageRequest.file(), createImageRequest.imageType());

            Image image = Image.create(createImageRequest.file().getOriginalFilename(), uploadedUrl, user);

            imageRepository.save(image);

            return new CreateImageResponse(image.getId(), image.getUrl());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
