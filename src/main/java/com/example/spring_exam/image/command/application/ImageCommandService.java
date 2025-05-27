package com.example.spring_exam.image.command.application;

import com.example.spring_exam.category.command.dto.ImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageResponse;
import com.example.spring_exam.user.domain.User;

import java.util.List;

public interface ImageCommandService {
    CreateImageResponse uploadImage(CreateImageRequest createImageRequest, User user);
    Long deleteImage(ImageRequest image);
    List<Long> deleteImage(List<ImageRequest> imageList);
}
