package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.externalInfo.UnivInfoResponseDto;
import com.simzoo.withmedical.service.CareerNetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UniversityApiController {

    private final CareerNetService careerNetService;

    @GetMapping("/universities")
    public ResponseEntity<UnivInfoResponseDto> getUniversities(@RequestParam String schoolName,
        @RequestParam String thisPage, @RequestParam String perPage) {

        return ResponseEntity.ok(
            careerNetService.getUniversities(thisPage, perPage, schoolName));
    }
}
