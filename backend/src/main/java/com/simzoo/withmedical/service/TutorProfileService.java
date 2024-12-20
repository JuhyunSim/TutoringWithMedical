package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto.TutorSearchFilter;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.tutor.TutorProfileJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TutorProfileService {

    private final TutorProfileJdbcRepository tutorProfileJdbcRepository;
    /**
     * 선생님 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<TutorSimpleResponseDto> getTutorList(Pageable pageable,
        TutorSearchFilter filterRequest) {
        return tutorProfileJdbcRepository.findFilteredProfiles(filterRequest, pageable);
    }

    /**
     * 선생님 개별조회
     */
    @Transactional(readOnly = true)
    public TutorProfileResponseDto getTutorDetail(Long tutorId) {
        return tutorProfileJdbcRepository.findTutorProfileDtoById(tutorId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
