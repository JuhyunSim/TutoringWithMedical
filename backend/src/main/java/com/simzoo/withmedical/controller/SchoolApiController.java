package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.univ.UnivInfoResponseDto;
import com.simzoo.withmedical.service.CareerNetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SchoolApiController {

    private final CareerNetService careerNetService;

    @GetMapping("/universities")
    public ResponseEntity<UnivInfoResponseDto> getUniversities(
        @RequestParam String schoolName,
        @RequestParam String thisPage,
        @RequestParam String perPage,
        @RequestParam String gubun,
        @RequestParam String schoolType1,
        @RequestParam String schoolType2) {

        return ResponseEntity.ok(
            careerNetService.getSchoolInfo(thisPage, perPage, schoolName, gubun, schoolType1,
                schoolType2));
    }
}
