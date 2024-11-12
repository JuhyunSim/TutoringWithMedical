package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.service.TutorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tutor")
public class TutorProfileController {

    private final TutorProfileService tutorProfileService;

    /**
     * 선생님 프로필 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<TutorSimpleResponseDto>> getTutorProfiles(
        @PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(tutorProfileService.getTutorList(pageable));
    }

    /**
     * 선생님 프로필 개별 조회
     */
    @GetMapping("/{tutorId}")
    public ResponseEntity<TutorProfileResponseDto> getTutorProfile(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorProfileService.getTutorDetail(tutorId));
    }
}
