package com.example.spring_exam.user.service;

import com.example.spring_exam.user.dto.LoginReq;
import com.example.spring_exam.user.dto.SignupReq;
import jakarta.validation.Valid;

public interface UserService {
    void signUp(SignupReq signupReq);
}
