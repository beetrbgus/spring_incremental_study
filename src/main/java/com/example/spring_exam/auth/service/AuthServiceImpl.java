package com.example.spring_exam.auth.service;

import com.example.spring_exam.auth.dto.UserTokenInfo;
import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.auth.util.JwtUtil;
import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.dto.LoginReq;
import com.example.spring_exam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Override
    public UserTokenResponse login(LoginReq loginReq) {
        User user = userRepository.findByUsername(loginReq.username())
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
        UserTokenInfo tokenInfo = new UserTokenInfo(user.getId(), user.getUsername(), user.getRole());

        String accessToken = jwtUtil.createAccessToken(tokenInfo);
        String refreshToken = jwtUtil.createRefreshToken(tokenInfo);

        refreshTokenService.saveRefreshToken(user.getUsername(), refreshToken, jwtUtil.getRefreshTokenTTL());

        return new UserTokenResponse(accessToken, refreshToken);
    }
}
