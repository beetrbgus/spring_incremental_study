package com.example.spring_exam.user.mapper;

import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.domain.UserRole;
import com.example.spring_exam.user.dto.SignupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User signupReqToEntity(SignupReq req) {
        return User.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .email(req.email())
                .nickname(req.nickname())
                .role(UserRole.USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }
}