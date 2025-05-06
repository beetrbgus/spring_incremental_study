package com.example.spring_exam.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken, long ttlMillis) {
        redisTemplate.opsForValue().set("refresh:" + username, refreshToken, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public Optional<String> getRefreshToken(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("refresh:" + username));
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh:" + username);
    }
}
