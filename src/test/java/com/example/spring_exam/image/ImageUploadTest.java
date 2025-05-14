package com.example.spring_exam.image;

import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.image.command.domain.ImageType;
import com.example.spring_exam.user.dto.LoginReq;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ImageUploadTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이미지 등록 - 성공")
    void image_upload_success() throws Exception {
        // 로그인 시도
        LoginReq testuser2 = new LoginReq("testuser2", "Test1234!2");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testuser2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.access_token").exists())
                .andReturn();

        // access token 추출
        String responseBody = loginResult.getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(CommonResponse.class, UserTokenResponse.class);

        CommonResponse<UserTokenResponse> result = objectMapper.readValue(responseBody, type);

        Path imagePath = Paths.get("C:\\Users\\beetr\\Pictures\\img_20220808115828_433c0652.webp");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "img_20220808115828_433c0652.webp",
                MediaType.IMAGE_JPEG_VALUE,
                imageBytes
        );

        // 이미지 업로드
        mockMvc.perform(
            multipart("/api/image")
                .file(file)
                .param("imageType", ImageType.CATEGORY.name())
                .param("isRepresentative", "true")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.data().getAccessToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.image_id").exists())
            .andExpect(jsonPath("$.data.url").exists());
    }
}
