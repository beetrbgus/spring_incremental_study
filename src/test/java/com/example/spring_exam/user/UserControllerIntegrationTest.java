package com.example.spring_exam.user;

import com.example.spring_exam.user.dto.SignupReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void testSignupSuccess() throws Exception {
        SignupReq signupReq = new SignupReq(
                "testuser",
                "Test1234!",
                "Tester",
                "testuser@example.com"
        );

        mockMvc.perform(
            post("/api/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signupReq))
        )
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 닉네임")
    void testSignupNicknameDuplicate() throws Exception {
        SignupReq signupReq = new SignupReq(
                "testuser2",
                "Test1234!2",
                "Tester2",
                "testuser2@example.com"
        );
        // user 추가
        mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        ).andExpect(status().isOk());

        // 중복 유저 추가
        mockMvc.perform(
            post("/api/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signupReq))
        )
        .andExpect(status().is5xxServerError());
    }
}
