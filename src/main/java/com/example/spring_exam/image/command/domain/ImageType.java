package com.example.spring_exam.image.command.domain;

import com.example.spring_exam.common.enums.EnumCode;
import com.example.spring_exam.common.enums.EnumCodeDeserializer;
import com.example.spring_exam.common.enums.EnumCodeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonSerialize(using = ImageType.ImageTypeSerializer.class)
@JsonDeserialize(using = ImageType.ImageTypeDeserializer.class)
@RequiredArgsConstructor
public enum ImageType implements EnumCode {
    CATEGORY("category", "카테고리"),
    PRODUCT("product", "상품"),
    ;

    private final String value;
    private final String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static class ImageTypeSerializer extends EnumCodeSerializer<ImageType> {}
    public static class ImageTypeDeserializer extends EnumCodeDeserializer<ImageType> {}
}
