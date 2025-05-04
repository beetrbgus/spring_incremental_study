package com.example.spring_exam.common.exception.auth;

import com.example.spring_exam.common.response.ErrorCode;

public class CustomAuthException extends AppException {
    public CustomAuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CustomAuthException(int statusCode) {
        super(statusCode);
    }
}
