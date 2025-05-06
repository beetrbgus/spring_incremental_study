package com.example.spring_exam.auth.util;

import com.example.spring_exam.common.exception.auth.UnAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticator {
    private final PasswordEncoder passwordEncoder;

    public void authenticate(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UnAuthenticatedException();
        }
    }
}