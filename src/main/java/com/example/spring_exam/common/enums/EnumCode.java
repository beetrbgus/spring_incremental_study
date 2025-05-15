package com.example.spring_exam.common.enums;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public interface EnumCode {
    @JsonValue
    String getValue();

    @JsonValue
    String getDisplayName();

    @JsonCreator
    static <E extends Enum<E> & EnumCode> E fromValue(Class<E> enumType, String value) {
        for (E enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }
        throw new AppException(ErrorCode.INVALID_INPUT_VALUE);
    }
}
