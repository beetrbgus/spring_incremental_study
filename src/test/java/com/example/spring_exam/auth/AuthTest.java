package com.example.spring_exam.auth;

import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.user.dto.LoginReq;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        LoginReq testuser2 = new LoginReq("testuser2", "Test1234!2");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testuser2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.access_token").exists())
                .andExpect(jsonPath("$.data.refresh_token").exists());
    }
    
    @Test
    @DisplayName("refresh 토큰을 이용한 Access 토큰 재발급 성공")
    void 만료시_AccessToken_재발행_성공() throws Exception {

        LoginReq testuser2 = new LoginReq("testuser2", "Test1234!2");

        // 로그인 요청 수행 후 응답에서 추출
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuser2)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CommonResponse.class, UserTokenResponse.class);

        CommonResponse<UserTokenResponse> result = objectMapper.readValue(responseBody, type);

        // accessToken 만료까지 sleep 테스트 yml의 Access 토큰 만료시간은 5초
        Thread.sleep(6000);

        UserTokenResponse userTokenResponse = result.data();

        assertNotNull(userTokenResponse, "로그인 한 뒤의 토큰 값은 Null이면 안 됨");
        assertNotNull(userTokenResponse.getAccessToken(), "로그인 한 뒤의 Access 토큰 값은 Null 이면 안 됨");
        assertNotNull(userTokenResponse.getRefreshToken(), "로그인 한 뒤의 Refresh 토큰 값은 Null 이면 안 됨");

        // refresh 재발급 요청 및 검증
        mockMvc.perform(
            post("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + userTokenResponse.getAccessToken())
            .header("Refresh-Token", userTokenResponse.getRefreshToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.access_token").exists())
            ;
    }

    @Test
    @DisplayName("refresh 토큰을 이용한 Access 토큰 재발급 실패 - refresh 만료")
    void 만료시_AccessToken_재발행_실패() throws Exception {
        LoginReq testuser2 = new LoginReq("testuser2", "Test1234!2");

        // 로그인 요청 수행 후 응답에서 추출
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuser2)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CommonResponse.class, UserTokenResponse.class);

        CommonResponse<UserTokenResponse> result = objectMapper.readValue(responseBody, type);

        UserTokenResponse userTokenResponse = result.data();

        // refresh 토큰 만료시키기
        Thread.sleep(5000);

        assertNotNull(userTokenResponse, "로그인 한 뒤의 토큰 값은 Null이면 안 됨");
        assertNotNull(userTokenResponse.getAccessToken(), "로그인 한 뒤의 Access 토큰 값은 Null 이면 안 됨");
        assertNotNull(userTokenResponse.getRefreshToken(), "로그인 한 뒤의 Refresh 토큰 값은 Null 이면 안 됨");

        // refresh 재발급 요청 및 검증
        mockMvc.perform(
                post("/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + userTokenResponse.getAccessToken())
                    .header("Refresh-Token", userTokenResponse.getRefreshToken()))
        .andExpect(status().isUnauthorized())
        ;
    }
}
