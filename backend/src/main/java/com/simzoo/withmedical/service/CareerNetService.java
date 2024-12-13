package com.simzoo.withmedical.service;

import com.simzoo.withmedical.client.CareerNetClient;
import com.simzoo.withmedical.dto.univ.UnivInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareerNetService {

    private final CareerNetClient careerNetClient;

    @Value("${careerNet.api.key}")
    private String UNIV_API_KEY;

    @Transactional
    public UnivInfoResponseDto getUniversities(String thisPage, String perPage,
        String searchSchoolName) {

        String svcType = "api";
        String svcCode = "SCHOOL";
        String gubun = "univ_list";
        String contentType = "Json";
        String schoolType1 = "100323";
        String schoolType2 = "100328";
        log.debug("apiKey: {}", UNIV_API_KEY);

        return careerNetClient.getUniversities(UNIV_API_KEY, svcType, svcCode, gubun, thisPage,
            perPage, searchSchoolName, contentType, schoolType1, schoolType2);
    }
}
