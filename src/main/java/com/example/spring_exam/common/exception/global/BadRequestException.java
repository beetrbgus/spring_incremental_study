package com.example.spring_exam.common.exception.global;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class BadRequestException extends AppException {

    public BadRequestException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(int statusCode) {
        super(statusCode);
    }
}
