package com.example.spring_exam.auth.controller;

import com.example.spring_exam.auth.dto.AccessTokenResponse;
import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.auth.service.AuthService;
import com.example.spring_exam.auth.util.TokenExtractor;
import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.user.dto.LoginReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenExtractor tokenExtractor;
    @PostMapping("/login")
    public CommonResponse<UserTokenResponse> login(@Valid @RequestBody LoginReq req) {
        UserTokenResponse token = authService.login(req);

        return CommonResponse.ok(token);
    }

    @PostMapping("/refresh")
    public CommonResponse<AccessTokenResponse> generateAccessToken(HttpServletRequest request) {
        String refreshToken = tokenExtractor.extractRefreshToken(request);

        AccessTokenResponse accessTokenResponse = authService.generateAccessToken(refreshToken);

        return CommonResponse.created(accessTokenResponse);
    }
}
