package com.example.spring_exam.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public record CommonResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionRes error
) {

    public static <T> CommonResponse<T> ok(@Nullable final T data) {
        return new CommonResponse<>(HttpStatus.OK, true, data, null);
    }

    public static <T> CommonResponse<T> created(@Nullable final T data) {
        return new CommonResponse<>(HttpStatus.CREATED, true, data, null);
    }

    public static <T> CommonResponse<T> fail(final ErrorCode c) {
        return new CommonResponse<>(c.getHttpStatus(), false, null, ExceptionRes.of(c));
    }
}