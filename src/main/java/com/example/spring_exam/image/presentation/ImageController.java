package com.example.spring_exam.image.presentation;

import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.image.command.application.ImageCommandService;
import com.example.spring_exam.image.command.dto.CreateImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageResponse;
import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.domain.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageCommandService imageCommandService;

    @PostMapping
    public CommonResponse<CreateImageResponse> uploadImage(@Valid CreateImageRequest createCategoryRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CreateImageResponse imageUrl = imageCommandService.uploadImage(createCategoryRequest, user);

        return CommonResponse.created(imageUrl);
    }
}
