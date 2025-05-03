package com.example.spring_exam.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN"),
    ;

    private final String key;

    public static UserRole fromString(String role) {
        try {
            return Arrays.stream(UserRole.values())
                    .filter(userRole -> role.contains(userRole.name()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + role));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("권한 정보가 올바르지 않습니다: " + role);
        }
    }
}
