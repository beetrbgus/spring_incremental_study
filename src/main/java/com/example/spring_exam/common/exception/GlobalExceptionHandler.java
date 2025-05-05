package com.example.spring_exam.common.exception;

import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.common.response.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // RequestBody & Valid 에서 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorCode invalidInputValue = ErrorCode.INVALID_INPUT_VALUE;

        return getResponse(invalidInputValue.getHttpStatus(), invalidInputValue, errors);
    }

    // @ModelAttribute & @Valid 에서 발생
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorCode invalidInputValue = ErrorCode.INVALID_INPUT_VALUE;

        return getResponse(invalidInputValue.getHttpStatus(), invalidInputValue, errors);
    }

    // @RequestParam 같은 단일 값 유효성 실패 시 발생
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            errors.put(field, violation.getMessage());
        });

        ErrorCode invalidInputValue = ErrorCode.INVALID_INPUT_VALUE;
        return getResponse(invalidInputValue.getHttpStatus(), invalidInputValue);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleAllExceptions(Exception ex) {
        ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return getResponse(internalServerError.getHttpStatus(), internalServerError, error);
    }


    private <T> ResponseEntity<CommonResponse<T>> getResponse(HttpStatus status, ErrorCode errorCode, T data) {
        if (data != null) {
            return ResponseEntity
                    .status(status)
                    .body(CommonResponse.fail(data, errorCode));
        } else {
            return ResponseEntity
                    .status(status)
                    .body(CommonResponse.fail(errorCode));
        }
    }

    private <T> ResponseEntity<CommonResponse<T>> getResponse(HttpStatus status, ErrorCode errorCode) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.fail(errorCode));
    }
}