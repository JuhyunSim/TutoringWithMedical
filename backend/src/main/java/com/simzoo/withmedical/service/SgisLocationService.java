package com.simzoo.withmedical.service;

import com.simzoo.withmedical.client.SgisClient;
import com.simzoo.withmedical.dto.location.StageLocationRequestDto;
import com.simzoo.withmedical.dto.location.StageLocationResponseDto;
import com.simzoo.withmedical.redis.SgisTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SgisLocationService {

    private final SgisClient sgisClient;
    private final SgisTokenManager sgisTokenManager;
    @Value("${sgis.api.consumerKey}")
    private String consumerKey;

    @Value("${sgis.api.consumerSecret}")
    private String consumerSecret;

    /**
     * 단계별 주소 조회
     */
    @Transactional
    public StageLocationResponseDto getStagedLocations(StageLocationRequestDto requestDto) {
        String accessToken = sgisTokenManager.getAccessToken(consumerKey, consumerSecret);
        log.debug("accessToken: {}", accessToken);

        if (requestDto.getCd().isEmpty()) {
            requestDto.setCdNull();
        }

        return sgisClient.getStageLocation(accessToken, requestDto.getCd(),
            requestDto.getPg_yn());
    }
}
