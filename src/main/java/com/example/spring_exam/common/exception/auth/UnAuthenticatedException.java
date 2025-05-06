package com.example.spring_exam.common.exception.auth;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class UnAuthenticatedException extends AppException {
    public UnAuthenticatedException() {
        super(ErrorCode.UNAUTHENTICATED_USER);
    }

    public UnAuthenticatedException(int statusCode) {
        super(statusCode);
    }
}
