package com.example.spring_exam.common.exception;

import com.example.spring_exam.common.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(int  statusCode) {
        this.errorCode = ErrorCode.findByStatus(statusCode);
    }

    public String getMessage() {
        return errorCode.getMessage();
    }
}
