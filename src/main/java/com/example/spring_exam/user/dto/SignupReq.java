package com.example.spring_exam.user.dto;

public record SignupReq(
    String username,
    String password,
    String nickname,
    String email
) {

}
