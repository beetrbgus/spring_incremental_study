package com.example.spring_exam.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHealthChecker {
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void checkRedisOnStartup() {
        try {
            if (isRedisAvailable()) {
                log.info("Redis 연결됨 ✅ ");
            } else {
                log.warn("Redis 연결 불가. 일부 기능이 제한될 수 있습니다. ❗ ");
            }
        } catch (Exception e) {
            log.error("Redis 초기 점검 실패: {}", e.getMessage());
        }
    }

    public boolean isRedisAvailable() {
        try {
            Optional<RedisConnectionFactory> factory = Optional.ofNullable(redisTemplate.getConnectionFactory());
            if (factory.isEmpty()) {
                log.warn("RedisConnectionFactory가 null입니다.");
                return false;
            }

            Optional<RedisConnection> connection = Optional.of(factory.get().getConnection());
            String ping = connection.get().ping();

            return RedisHealthCheckType.PONG.name().equalsIgnoreCase(ping);
        } catch (Exception e) {
            log.error("Redis 상태 점검 실패: {}", e.getMessage());
            return false;
        }
    }
}

enum RedisHealthCheckType {
    PING,
    PONG,
}