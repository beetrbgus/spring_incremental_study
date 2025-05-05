package com.example.spring_exam.common.exception.user;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class UserNotFoundException extends AppException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(int statusCode) {
        super(statusCode);
    }
}
