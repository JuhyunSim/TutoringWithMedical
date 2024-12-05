package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorProfileRepositoryCustom {
    Page<TutorSimpleResponseDto> findTutorProfileDtos(Pageable pageable, TutorFilterRequestDto tutorFilterRequestDto);

    Optional<TutorProfileResponseDto> findTutorProfileDtoById(Long id);
}
