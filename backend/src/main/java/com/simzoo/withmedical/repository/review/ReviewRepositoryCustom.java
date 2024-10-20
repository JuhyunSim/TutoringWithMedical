package com.simzoo.withmedical.repository.review;

import com.simzoo.withmedical.dto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ReviewResponseDto> findReviewsByTutorId(Long tutorId, Pageable pageable);
}
