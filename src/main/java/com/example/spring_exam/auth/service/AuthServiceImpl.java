package com.example.spring_exam.auth.service;

import com.example.spring_exam.auth.dto.AccessTokenResponse;
import com.example.spring_exam.auth.dto.UserTokenInfo;
import com.example.spring_exam.auth.dto.UserTokenResponse;
import com.example.spring_exam.auth.util.JwtUtil;
import com.example.spring_exam.auth.util.UserAuthenticator;
import com.example.spring_exam.common.exception.AppException;
import com.example.spring_exam.common.exception.auth.CustomJwtTokenException;
import com.example.spring_exam.common.exception.auth.UnAuthenticatedException;
import com.example.spring_exam.common.exception.auth.UnAuthorizedException;
import com.example.spring_exam.common.exception.global.BadRequestException;
import com.example.spring_exam.common.exception.user.UserNotFoundException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.dto.LoginReq;
import com.example.spring_exam.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserAuthenticator authenticator;

    @Override
    public UserTokenResponse login(LoginReq loginReq) {
        User user = userRepository.findByUsername(loginReq.username())
                                    .orElseThrow(UserNotFoundException::new);
        // 비밀번호 일치 검사
        authenticator.authenticate(loginReq.password(), user.getPassword());

        UserTokenInfo tokenInfo = new UserTokenInfo(user.getId(), user.getUsername(), user.getRole());

        String accessToken = jwtUtil.createAccessToken(tokenInfo);
        String refreshToken = jwtUtil.createRefreshToken(tokenInfo);

        refreshTokenService.saveRefreshToken(user.getUsername(), refreshToken, jwtUtil.getRefreshTokenTTL());

        return new UserTokenResponse(accessToken, refreshToken);
    }

    @Override
    public AccessTokenResponse generateAccessToken(String refreshToken) {
        try {
            if(!StringUtils.hasText(refreshToken)) {
                throw new BadRequestException();
            }

            // 만료되지 않은 토큰으로 사용자 명 조회
            String username = jwtUtil.getUsernameByRefreshToken(refreshToken);

            // Redis에서 refresh Token 가져옴 (삭제되었다면 로그인 다시 필요)
            refreshTokenService
                .getRefreshToken(username)
                .orElseThrow(UnAuthenticatedException::new);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(UserNotFoundException::new);

            // 새 Access Token 발급
            UserTokenInfo userTokenInfo = new UserTokenInfo(user.getId(), username, user.getRole());

            String newAccessToken = jwtUtil.createAccessToken(userTokenInfo);

            return AccessTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .build();

        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException();
        } catch (MalformedJwtException e) {
            throw new CustomJwtTokenException(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    @Override
    public void logout(String accessToken) {
        if(!StringUtils.hasText(accessToken)) {
            throw new BadRequestException();
        }
        log.info("Access Token: {}", accessToken);

        try {
            UserTokenInfo userInfo = jwtUtil.getUserInfo(accessToken);

            refreshTokenService.deleteRefreshToken(userInfo.username());
        } catch (Exception e) {
            log.error("로그아웃 중 오류 발생", e);

            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
