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
import java.util.Objects;

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
        String accessToken = getAccessToken("testuser2", "Test1234!2");

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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.image_id").exists())
            .andExpect(jsonPath("$.data.url").exists());
    }

    @Test
    @DisplayName("이미지 등록 - 잘못된 파일 형식")
    void image_upload_invalid_file_type() throws Exception {
        // 로그인 시도
        String accessToken = getAccessToken("testuser2", "Test1234!2");

        // 잘못된 형식의 파일 (예: PDF 파일)
        Path imagePath = Paths.get("C:\\Users\\beetr\\Documents\\카카오톡 받은 파일\\김유정-봄·봄-조광.pdf");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "sample.pdf",
                "application/pdf",
                imageBytes
        );

        // 이미지 업로드 시도 (잘못된 형식)
        mockMvc.perform(
            multipart("/api/image")
                .file(file)
                .param("imageType", ImageType.CATEGORY.name())
                .param("isRepresentative", "true")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.message").exists());
    }

    @Test
    @DisplayName("이미지 등록 - imageType 누락")
    void image_upload_missing_image_type() throws Exception {
        // 로그인 시도
        String accessToken = getAccessToken("testuser2", "Test1234!2");

        // 이미지 파일 준비
        Path imagePath = Paths.get("C:\\Users\\beetr\\Pictures\\img_20220808115828_433c0652.webp");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "img_20220808115828_433c0652.webp",
                MediaType.IMAGE_JPEG_VALUE,
                imageBytes
        );

        // imageType 파라미터를 누락하여 업로드 시도
        mockMvc.perform(
                        multipart("/api/image")
                                .file(file)
                                .param("isRepresentative", "true")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").isNotEmpty());
    }

    @Test
    @DisplayName("이미지 등록 - 파일이 없는 경우")
    void image_upload_no_file() throws Exception {
        // 로그인 시도
        String accessToken = getAccessToken("testuser2", "Test1234!2");

        // 파일 없이 업로드 시도
        mockMvc.perform(
                        multipart("/api/image")
                                .param("imageType", ImageType.CATEGORY.name())
                                .param("isRepresentative", "true")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").value("파일을 첨부해야 합니다."));
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
