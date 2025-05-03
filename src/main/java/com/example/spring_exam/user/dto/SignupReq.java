package com.example.spring_exam.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupReq(
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 30, message = "비밀번호는 8~30자여야 합니다.")
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
        String nickname,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email
) {

}
