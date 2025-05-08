package com.example.spring_exam.common.filter;

import com.example.spring_exam.auth.dto.UserTokenInfo;
import com.example.spring_exam.auth.service.RefreshTokenService;
import com.example.spring_exam.auth.service.UserDetailsServiceImpl;
import com.example.spring_exam.auth.util.JwtUtil;
import com.example.spring_exam.auth.util.TokenExtractor;
import com.example.spring_exam.common.response.CommonResponse;
import com.example.spring_exam.common.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final TokenExtractor tokenExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenExtractor.extractAccessToken(request);

        if (accessToken != null) {
            try {
                UserTokenInfo userInfo = jwtUtil.getUserInfo(accessToken);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userInfo.role().getKey()));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userInfo, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                // 토큰이 만료되었을 때 요청 속성에 더 자세한 정보를 담아서 전달
                request.setAttribute("expired", true);
                request.setAttribute("expiredToken", accessToken); // 만료된 토큰 정보
                request.setAttribute("expiredTokenClaims", e.getClaims()); // 만료된 토큰의 클레임 정보
            } catch (Exception e) {
                handleInvalidToken(response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleInvalidToken(HttpServletResponse response, Exception e) throws IOException {
        log.error("유효하지 않은 토큰", e);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        CommonResponse<Void> errorResponse = CommonResponse.fail(ErrorCode.INVALID_TOKEN);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
