package com.example.spring_exam.user.controller;

import com.example.spring_exam.user.dto.SignupReq;
import com.example.spring_exam.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping
    public void signUp(@RequestBody SignupReq signupReq) {
        authService.signUp(signupReq);
    }
}
