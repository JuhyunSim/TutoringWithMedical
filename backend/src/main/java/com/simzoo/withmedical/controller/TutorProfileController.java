package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.service.TutorProfileService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        @ModelAttribute TutorFilterRequestDto filterRequest,
        @PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "createdAt") Pageable pageable) {

        TutorFilterRequestDto.TutorEnumFilter filter = TutorFilterRequestDto.TutorEnumFilter.builder()
            .gender(filterRequest.convertGender(filterRequest.getGender()))
            .subjects(filterRequest.convertSubjects(filterRequest.getSubjects()))
            .locations(filterRequest.convertLocations(filterRequest.getLocations()))
            .universities(filterRequest.convertUniversity(filterRequest.getUniversities()))
            .statusList(filterRequest.convertEnrollmentStatus(filterRequest.getStatusList()))
            .build();

        return ResponseEntity.ok(tutorProfileService.getTutorList(pageable, filter));
    }

    /**
     * 선생님 프로필 개별 조회
     */
    @GetMapping("/{tutorId}")
    public ResponseEntity<TutorProfileResponseDto> getTutorProfile(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorProfileService.getTutorDetail(tutorId));
    }

    /**
     * 선생님 조회 필터요청값 불러오기
     */
    @GetMapping("/filters")
    public ResponseEntity<Map<java.lang.String, List<java.lang.String>>> getFilters() {

        Map<java.lang.String, List<java.lang.String>> filters = new HashMap<>();

        filters.put("subjects", Arrays.stream(Subject.values()).map(Subject::getDescription).toList());
        filters.put("locations", Arrays.stream(Location.values()).map(Location::getDescription).toList());
        filters.put("universities", Arrays.stream(University.values()).map(University::getDescription).toList());
        filters.put("statusList", Arrays.stream(EnrollmentStatus.values()).map(EnrollmentStatus::getDescription).toList());
        filters.put("gender", Arrays.stream(Gender.values()).map(Gender::getDescription).toList());

        return ResponseEntity.ok(filters);
    }
}
