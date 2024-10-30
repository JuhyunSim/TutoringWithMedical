package com.simzoo.withmedical.service;

import com.simzoo.withmedical.util.JwtUtil;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Transactional
    public void logout(String token) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        long expirationTime = jwtUtil.extractExpiration(token) - Instant.now().toEpochMilli(); // 남은 유효 시간 계산
        ops.set(token, "logout", Duration.ofMillis(expirationTime));
    }

    @Transactional
    public boolean isLoggedOut(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
