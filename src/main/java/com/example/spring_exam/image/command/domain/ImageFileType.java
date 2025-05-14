package com.example.spring_exam.image.command.domain;

import com.example.spring_exam.common.enums.EnumCode;
import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ImageFileType implements EnumCode {
    JPEG("image/jpeg", ".jpeg"),
    JPG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    WEBP("image/webp", ".webp"),
    GIF("image/gif", ".gif"),
    SVG("image/svg+xml", ".svg"),
    AVIF("image/avif", ".avif")
    ;

    private final String value;
    private final String extension;

    @Override
    public String getValue() {
        return this.value;
    }

    public static ImageFileType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE));
    }

    public static boolean isSupported(String mimeType) {
        return Arrays.stream(values())
                .anyMatch(type -> type.getValue().equalsIgnoreCase(mimeType));
    }
}
