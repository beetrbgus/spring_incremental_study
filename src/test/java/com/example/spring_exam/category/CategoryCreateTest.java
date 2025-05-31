package com.example.spring_exam.category;

import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.category.command.dto.CreateCategoryRequest;
import com.example.spring_exam.category.command.dto.ImageRequest;
import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.user.dto.LoginReq;
import com.fasterxml.jackson.databind.JavaType;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryCreateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 생성 - 성공")
    void createCategorySuccess() throws Exception {
        String accessToken = getAccessToken("testuser2", "Test1234!2");

        CreateCategoryRequest request = new CreateCategoryRequest(
            "간장",
            2,
            0,
            "간장의 종류에는 양조, 진간장, 국간장 등이 있다.",
            true,
            null,
            2L
        );

        mockMvc.perform(post("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
        ;

    }
    
    @Test
    @DisplayName("카테고리 생성 이름 중복 - 실패")
    void createCategoryFailWhenDuplicate() throws Exception {
        String accessToken = getAccessToken("testuser2", "Test1234!2");

        CreateCategoryRequest request = new CreateCategoryRequest(
            "장/소스/드레싱/식초",
            0,
            0,
            "장 소스 드레싱 식초",
            true,
            new ImageRequest(
            1L,
            "https://pub-19287201346a46b1adc5270fbbc3fad9.r2.dev/category/2025-05-15/a68b43d4-6513-4a43-9ab0-b8031b763020.webp"
            ),
            null
        );

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(50700))
        ;
    }

    private String getAccessToken(String username, String password) throws Exception {
        LoginReq loginReq = new LoginReq(username, password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CommonResponse.class, UserTokenResponse.class);

        CommonResponse<UserTokenResponse> result = objectMapper.readValue(responseBody, type);

        return Objects.requireNonNull(result.data()).getAccessToken();
    }
}
