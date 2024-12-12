package com.simzoo.withmedical.service;

import com.simzoo.withmedical.client.SgisClient;
import com.simzoo.withmedical.dto.externalInfo.SgisToken;
import com.simzoo.withmedical.dto.externalInfo.location.StageLocationRequestDto;
import com.simzoo.withmedical.dto.externalInfo.location.StageLocationResponseDto;
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

    @Value("${sgis.api.consumerKey}")
    private String consumerKey;

    @Value("${sgis.api.consumerSecret}")
    private String consumerSecret;

    /**
     * 단계별 주소 조회
     */
    @Transactional
    public StageLocationResponseDto getStagedLocations(StageLocationRequestDto requestDto) {
        String accessToken = ensureAccessToken(consumerKey, consumerSecret).getResult()
            .getAccessToken();
        log.debug("accessToken: {}", accessToken);

        if (requestDto.getCd().isEmpty()) {
            requestDto.setCdNull();
        }

        return sgisClient.getStageLocation(accessToken, requestDto.getCd(),
            requestDto.getPg_yn());
    }

    /**
     * SGIS AccessToken 발급 또는 갱신
     */
    private SgisToken ensureAccessToken(String consumerKey, String consumerSecret) {
        return sgisClient.getAccessToken(consumerKey, consumerSecret);
    }
}
