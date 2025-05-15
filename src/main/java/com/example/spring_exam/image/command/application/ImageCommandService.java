package com.example.spring_exam.image.command.application;

import com.example.spring_exam.image.command.dto.CreateImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageResponse;
import com.example.spring_exam.user.domain.User;

public interface ImageCommandService {
    CreateImageResponse uploadImage(CreateImageRequest createImageRequest, User user);
}
