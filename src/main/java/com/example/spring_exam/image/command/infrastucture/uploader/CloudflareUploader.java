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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudflareUploader implements ImageUploader {
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
}
