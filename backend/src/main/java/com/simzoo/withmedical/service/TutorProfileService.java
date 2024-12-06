package com.simzoo.withmedical.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TutorProfileService {

    private final TutorProfileRepository tutorProfileRepository;
    private final ObjectMapper objectMapper;
    /**
     * 선생님 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<TutorSimpleResponseDto> getTutorList(Pageable pageable,
        TutorFilterRequestDto filterRequest) {

        return tutorProfileRepository.findFilteredProfiles(filterRequest, pageable);
    }

    /**
     * 선생님 개별조회
     */
    @Transactional(readOnly = true)
    public TutorProfileResponseDto getTutorDetail(Long tutorId) {
        return tutorProfileRepository.findTutorProfileDtoById(tutorId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private List<TutorSimpleResponseDto> mapToDto(List<Object[]> results) {
        return results.stream()
            .map(row -> new TutorSimpleResponseDto(
                (Long) row[0],         // tutor_id
                (String) row[1],       // image_url
                (String) row[2],       // tutor_nickname
                (University) row[3],       // tutor_university
                (Location) row[4],       // tutor_location
                Arrays.asList((Subject[]) row[5]) // subjects as List<String>
            ))
            .toList();
    }
}
