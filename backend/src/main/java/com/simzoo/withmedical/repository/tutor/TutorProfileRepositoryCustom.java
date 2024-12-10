package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorProfileRepositoryCustom {

    Long countFilteredProfiles(TutorFilterRequestDto filterRequestDto);

    Page<TutorSimpleResponseDto> findFilteredProfiles(TutorFilterRequestDto filterRequestDto, Pageable pageable);

    Optional<TutorProfileResponseDto> findTutorProfileDtoById(Long id);
}
