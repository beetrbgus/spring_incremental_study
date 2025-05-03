package com.example.spring_exam.user.controller;

import com.example.spring_exam.user.dto.LoginReq;
import com.example.spring_exam.user.dto.SignupReq;
import com.example.spring_exam.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody SignupReq signupReq) {
        userService.signUp(signupReq);
    }
}
