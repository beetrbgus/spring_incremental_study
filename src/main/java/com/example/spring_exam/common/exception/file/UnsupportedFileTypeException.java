package com.example.spring_exam.common.exception.file;

import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.response.ErrorCode;

public class UnsupportedFileTypeException extends AppException {
    public UnsupportedFileTypeException() {
        super(ErrorCode.UPLOAD_FILE_NOT_FOUND);
    }
}