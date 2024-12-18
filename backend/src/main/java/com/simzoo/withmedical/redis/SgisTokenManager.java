package com.simzoo.withmedical.redis;

import com.simzoo.withmedical.client.SgisClient;
import com.simzoo.withmedical.dto.token.SgisToken;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SgisTokenManager {

    private final StringRedisTemplate redisTemplate;
    private final SgisClient sgisClient;

    private static final String ACCESS_TOKEN_KEY = "sgis:accessToken";

    /**
     * AccessToken을 가져오고, 만료되었으면 갱신
     */
    public String getAccessToken(String consumerKey, String consumerSecret) {
        // Redis에서 AccessToken 조회
        String accessToken = redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);

        if (accessToken == null) {
            // 토큰이 없거나 만료된 경우 새로 발급
            accessToken = fetchNewAccessToken(consumerKey, consumerSecret);
        }

        return accessToken;
    }

    /**
     * SGIS API를 호출하여 새로운 AccessToken 발급
     */
    private String fetchNewAccessToken(String consumerKey, String consumerSecret) {
        SgisToken sgisToken = sgisClient.getAccessToken(consumerKey, consumerSecret);

        String accessToken = sgisToken.getResult().getAccessToken();
        Long accessTimeout = Long.parseLong(sgisToken.getResult().getAccessTimeout());
        log.debug("accessTimeout = {}", accessTimeout);

        Long now = Instant.now().toEpochMilli();
        log.debug("now = {}", now);

        Long expiresIn = accessTimeout - now;
        log.debug("expiresIn = {}", expiresIn);

        // Redis에 저장 (TTL: 4시간)
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY, accessToken, expiresIn, TimeUnit.MILLISECONDS);

        return accessToken;
    }
}
