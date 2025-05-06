package com.example.spring_exam.common.response;

import jakarta.validation.constraints.NotNull;

public record ExceptionRes(
    @NotNull Integer code,
    @NotNull String message) {

    public static ExceptionRes of(ErrorCode errorCode) {
        return new ExceptionRes(
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }
}