package com.example.spring_exam.common.exception.auth;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class UnAuthorizedException extends AppException {
    public UnAuthorizedException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    public UnAuthorizedException(int statusCode) {
        super(statusCode);
    }
}
