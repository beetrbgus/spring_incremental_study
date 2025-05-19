package com.example.spring_exam.common.exception.global;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class ExternalApiException extends AppException {

    public ExternalApiException() {
        super(ErrorCode.EXTERNAL_REMOTE_COMMUNICATION);
    }

    public ExternalApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
