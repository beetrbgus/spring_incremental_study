package com.example.spring_exam.category;

import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.category.command.dto.CreateCategoryRequest;
import com.example.spring_exam.category.command.dto.ImageRequest;
import com.example.spring_exam.category.query.dto.res.CategoryListRes;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryGetTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 전체 조회 - 성공")
    void createCategorySuccess() throws Exception {

        MvcResult result =
            mockMvc.perform(get("/api/category/query")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + getAccessToken())
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CommonResponse.class, CategoryListRes.class);

        CommonResponse<CategoryListRes> categoryListRes = objectMapper.readValue(responseBody, type);

        CategoryListRes data = categoryListRes.data();
    }

    private String getAccessToken() throws Exception {
        LoginReq loginReq = new LoginReq("testuser2", "Test1234!2");

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
