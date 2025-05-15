package com.example.spring_exam.auth.service;

import com.example.spring_exam.auth.dto.AccessTokenResponse;
import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.user.dto.LoginReq;

public interface AuthService {
    UserTokenResponse login(LoginReq loginReq);

    AccessTokenResponse generateAccessToken(String accessToken);

    void logout(String accessToken);
}
