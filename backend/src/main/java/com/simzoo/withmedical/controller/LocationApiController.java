package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.externalInfo.location.StageLocationRequestDto;
import com.simzoo.withmedical.dto.externalInfo.location.StageLocationResponseDto;
import com.simzoo.withmedical.service.SgisLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationApiController {

    private final SgisLocationService sgisLocationService;

    @GetMapping("/locations")
    public ResponseEntity<StageLocationResponseDto> getLocations(
        @ModelAttribute StageLocationRequestDto requestDto) {
        return ResponseEntity.ok(sgisLocationService.getStagedLocations(requestDto));
    }
}
