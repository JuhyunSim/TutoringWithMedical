package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorProfileRepositoryCustom {
    Page<TutorSimpleResponseDto> findTutorProfileDtos(Pageable pageable);
}
