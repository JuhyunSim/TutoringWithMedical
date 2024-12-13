package com.simzoo.withmedical.client;

import com.simzoo.withmedical.dto.token.SgisToken;
import com.simzoo.withmedical.dto.location.StageLocationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sgisClient", url = "https://sgisapi.kostat.go.kr/OpenAPI3")
public interface SgisClient {

    @GetMapping("/auth/authentication.json")
    SgisToken getAccessToken(
        @RequestParam String consumer_key,
        @RequestParam String consumer_secret
    );

    @GetMapping("/addr/stage.json")
    StageLocationResponseDto getStageLocation(
        @RequestParam String accessToken,
        @RequestParam(required = false) String cd,
        @RequestParam(defaultValue = "0") String pg_yn
    );
}
