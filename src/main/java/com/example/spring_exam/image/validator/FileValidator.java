package com.example.spring_exam.image.validator;

import com.example.spring_exam.common.exception.file.FileNotFoundException;
import com.example.spring_exam.common.exception.file.UnsupportedFileTypeException;
import com.example.spring_exam.image.command.domain.ImageFileType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {
    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException();
        }

        String contentType = file.getContentType();

        if (!ImageFileType.isSupported(contentType)) {
            throw new UnsupportedFileTypeException();
        }
    }
}