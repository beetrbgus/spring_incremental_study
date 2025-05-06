package com.example.spring_exam.common.exception.auth;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class CustomJwtTokenException extends AppException {

    public CustomJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CustomJwtTokenException(int statusCode) {
        super(statusCode);
    }
}
