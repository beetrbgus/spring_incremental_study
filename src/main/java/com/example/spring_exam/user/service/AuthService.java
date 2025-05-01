package com.example.spring_exam.user.service;

import com.example.spring_exam.user.dto.SignupReq;

public interface AuthService {
    void signUp(SignupReq signupReq);
}
