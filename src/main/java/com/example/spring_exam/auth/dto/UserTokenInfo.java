package com.example.spring_exam.auth.dto;

import com.example.spring_exam.user.domain.UserRole;

public record UserTokenInfo(
    Long id,
    String username,
    UserRole role
) {}
