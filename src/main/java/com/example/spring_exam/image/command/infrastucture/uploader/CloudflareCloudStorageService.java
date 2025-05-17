package com.example.spring_exam.image.command.infrastucture.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.image.command.domain.ImageType;
import com.example.spring_exam.image.command.infrastucture.config.R2StorageProperties;
import com.example.spring_exam.image.utils.ImageUtils;
import com.example.spring_exam.image.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudflareCloudStorageService implements ImageCloudStorageService {
    private final R2StorageProperties properties;
    private final AmazonS3 client;
    private final FileValidator fileValidator;

    @Override
    public String upload(MultipartFile file, ImageType imageType) {
        try {
            log.info("Cloudflare 파일 업로드 진입");

            // 파일 검증
            fileValidator.validate(file);

            String contentType = file.getContentType();
            log.debug("contentType : {}", contentType);

            String extension = ImageUtils.extractExtension(file.getOriginalFilename());
            log.debug("extension : {}", extension);
            String path = ImageUtils.buildFilePath(imageType, extension);
            log.debug("path : {}", path);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // ACL을 public-read로 설정
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    properties.getImageBucket(),
                    path,
                    file.getInputStream(),
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            client.putObject(putObjectRequest);

            log.info("파일 업로드 성공 ✅ url : {} {}", properties.getImagePublic(), path);

            return properties.getImagePublic() + "/" + path;
        } catch (IOException e) {
            log.error("Cloudflare 파일 업로드 실패: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public void deleteImage(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            log.warn("삭제할 이미지 URL이 없습니다.");
            return;
        }

        log.info("Cloudflare 파일 삭제 진입: {} 개의 이미지", imageUrlList.size());

        try {
            for (String imageUrl : imageUrlList) {
                // URL에서 객체 키(path) 추출
                String objectKey = extractObjectKeyFromUrl(imageUrl);

                if (!StringUtils.hasText(objectKey)) {
                    log.warn("유효하지 않은 이미지 URL 형식: {}", imageUrl);
                    continue;
                }

                log.debug("삭제할 객체 키: {}", objectKey);

                // R2에서 객체 삭제
                client.deleteObject(properties.getImageBucket(), objectKey);

                log.info("파일 삭제 성공 ✅ : {}", objectKey);
            }
        } catch (Exception e) {
            log.error("Cloudflare 파일 삭제 실패: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    /**
     * 이미지 URL에서 객체 키(path)를 추출하는 메서드
     */
    private String extractObjectKeyFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        // 공개 URL에서 객체 키 추출
        String publicUrl = properties.getImagePublic();
        if (imageUrl.startsWith(publicUrl)) {
            // URL에서 publicUrl 부분을 제거하고 앞에 있는 슬래시도 제거
            String objectKey = imageUrl.substring(publicUrl.length());
            // 앞에 슬래시가 있다면 제거
            if (objectKey.startsWith("/")) {
                objectKey = objectKey.substring(1);
            }
            return objectKey;
        }

        // URL 형식이 다른 경우 전체 경로를 반환
        log.warn("예상치 못한 URL 형식: {}", imageUrl);
        return null;
    }
}
