package com.simzoo.withmedical.client;

import com.simzoo.withmedical.dto.univ.UnivInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "careerNetClient", url = "https://www.career.go.kr")
public interface CareerNetClient {
    @GetMapping("/cnet/openapi/getOpenApi.json")
    UnivInfoResponseDto getUniversities(
        @RequestParam String apiKey,
        @RequestParam String svcType,
        @RequestParam String svcCode,
        @RequestParam String gubun,
        @RequestParam String thisPage,
        @RequestParam String perPage,
        @RequestParam String searchSchulNm,
        @RequestParam(defaultValue = "Json") String contentType,
        @RequestParam String sch1,
        @RequestParam String sch2
    );
}