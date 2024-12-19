package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto.TutorSearchFilter;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import java.util.Optional;

public interface TutorProfileRepositoryCustom {

    Long countFilteredProfiles(TutorSearchFilter filterRequestDto);

    Optional<TutorProfileResponseDto> findTutorProfileDtoById(Long id);
}
