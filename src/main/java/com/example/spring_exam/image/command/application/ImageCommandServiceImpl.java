package com.example.spring_exam.image.command.application;

import com.example.spring_exam.category.command.dto.ImageRequest;
import com.example.spring_exam.common.exception.file.FileNotFoundException;
import com.example.spring_exam.common.exception.global.BadRequestException;
import com.example.spring_exam.image.command.domain.Image;
import com.example.spring_exam.image.command.dto.CreateImageRequest;
import com.example.spring_exam.image.command.dto.CreateImageResponse;
import com.example.spring_exam.image.command.infrastucture.persistence.ImageRepository;
import com.example.spring_exam.image.command.infrastucture.uploader.ImageCloudStorageService;
import com.example.spring_exam.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ImageCommandServiceImpl implements ImageCommandService {
    private final ImageRepository imageRepository;
    private final ImageCloudStorageService imageCloudStorageService;

    @Override
    public CreateImageResponse uploadImage(CreateImageRequest createImageRequest, User user) {
        String uploadedUrl = imageCloudStorageService.upload(createImageRequest.file(), createImageRequest.imageType());
        Image image = Image.create(createImageRequest.file().getOriginalFilename(), uploadedUrl, user);

        imageRepository.save(image);

        return new CreateImageResponse(image.getId(), image.getUrl());
    }

    @Override
    public Long deleteImage(ImageRequest image) {
        validateAndDeleteImages(List.of(image));

        imageCloudStorageService.deleteImage(image.url());
        imageRepository.deleteById(image.imageId());

        return image.imageId();
    }

    /**
     *
     * @param imageRequestList
     * @return 삭제되지 않은 이미지 반환
     */
    @Override
    public List<Long> deleteImage(List<ImageRequest> imageRequestList) {
        validateAndDeleteImages(imageRequestList);

        List<String> imageUrlList = imageRequestList.stream().map(ImageRequest::url).toList();

        List<String> deleteImageUrls = imageCloudStorageService.deleteImages(imageUrlList);

        List<Long> successDeleteIds = imageRequestList.stream()
                .filter(imageRequest -> deleteImageUrls.contains(imageRequest.url()))
                .map(ImageRequest::imageId)
                .toList();
        // TODO 삭제 실패 된 것들은 재시도 처리로 리팩토링

        imageRepository.deleteAllById(successDeleteIds);

        return List.of();
    }

    private void validateAndDeleteImages(List<ImageRequest> imageRequestList) {
        List<Long> imageIdList = imageRequestList.stream()
                .map(ImageRequest::imageId)
                .toList();

        List<Image> imageList = imageRepository.findAllById(imageIdList);

        if (imageList.size() != imageIdList.size()) {
            throw new FileNotFoundException();
        }

        Map<Long, String> requestUrlMap = imageRequestList.stream()
                .collect(Collectors.toMap(ImageRequest::imageId, ImageRequest::url));

        // URL 일치 검증
        imageList.stream()
            .filter(image -> !image.getUrl().equals(requestUrlMap.get(image.getId())))
            .findFirst()
            .ifPresent(image -> {
                throw new BadRequestException();
            });
    }
}
