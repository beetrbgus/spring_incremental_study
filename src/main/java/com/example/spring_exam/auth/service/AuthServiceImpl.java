package com.example.spring_exam.auth.service;

import com.example.spring_exam.auth.dto.AccessTokenResponse;
import com.example.spring_exam.auth.dto.UserTokenInfo;
import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.auth.util.JwtUtil;
import com.example.spring_exam.common.exception.auth.CustomJwtTokenException;
import com.example.spring_exam.common.exception.auth.UnAuthorizedException;
import com.example.spring_exam.common.exception.user.UserNotFoundException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.dto.LoginReq;
import com.example.spring_exam.user.repository.UserRepository;
import io.jsonwebtoken.MalformedJwtException;
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

    @Override
    public AccessTokenResponse generateAccessToken(String refreshToken) {
        try {
            if(!jwtUtil.validationToken(refreshToken)) {
                throw new UnAuthorizedException();
            }

            // Redis에서 refresh Token 가져옴 (삭제되었다면 로그인 다시 필요)
            String redisRefreshToken = refreshTokenService
                    .getRefreshToken(refreshToken)
                    .orElseThrow(UnAuthorizedException::new);

            // 만료되지 않은 토큰으로 사용자 명 조회
            String username = jwtUtil.getUsernameByRefreshToken(redisRefreshToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(UserNotFoundException::new);

            // 새 Access Token 발급
            UserTokenInfo userTokenInfo = new UserTokenInfo(user.getId(), username, user.getRole());

            String newAccessToken = jwtUtil.createAccessToken(userTokenInfo);

            return AccessTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .build();

        } catch (MalformedJwtException e) {
            throw new CustomJwtTokenException(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }
}
