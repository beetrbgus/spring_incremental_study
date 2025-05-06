package com.example.spring_exam.common.response;

import com.example.spring_exam.common.enums.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorCode implements EnumCode {
    // Common Error
    INVALID_INPUT_VALUE(40001, HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(40002, HttpStatus.BAD_REQUEST, "잘못된 파라미터 타입입니다."),
    NOT_FOUND_END_POINT(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),

    // Server Error
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // User
    USER_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(40901, HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),

    // Token
    INVALID_TOKEN(40100, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(40102, HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    WRONG_TOKEN(40103, HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    EMPTY_TOKEN(40104, HttpStatus.UNAUTHORIZED, "토큰이 비어있습니다."),
    UNAUTHORIZED_ACCESS(40105, HttpStatus.UNAUTHORIZED, "인가되지 않은 접근입니다."),
    UNAUTHENTICATED_USER(40105, HttpStatus.FORBIDDEN, "인증되지 않은 사용자입니다."),
    FORBIDDEN_ACCESS_ADMIN(40300, HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),

    ;
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorCode findByStatus(int statusCode) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.code == statusCode)
                .findFirst()
                .orElse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String getValue() {
        return message;
    }
}
