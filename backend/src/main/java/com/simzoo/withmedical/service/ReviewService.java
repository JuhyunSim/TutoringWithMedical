package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.ReviewRequestDto;
import com.simzoo.withmedical.dto.ReviewResponseDto;
import com.simzoo.withmedical.dto.UpdateReviewRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.ReviewEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewEntity saveReview(Long userId, ReviewRequestDto requestDto) {

        reviewRepository.findByTutorProfile_IdAndWriter_Id(requestDto.getTutorProfileId(), userId)
            .ifPresent(existingReview -> {
                throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
            });

        MemberEntity tutor = memberRepository.findByTutorProfile_Id(
                requestDto.getTutorProfileId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        MemberEntity writer = memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return reviewRepository.save(requestDto.toEntity(tutor, writer));
    }

    @Transactional
    public ReviewEntity changeReview(Long userId, Long reviewId, UpdateReviewRequestDto requestDto) {

        ReviewEntity reviewEntity = reviewRepository.findByIdAndWriter_Id(reviewId, userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        reviewEntity.update(requestDto);

        return reviewEntity;
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getTutorReviews(Long tutorId, Pageable pageable) {

        return reviewRepository.findReviewsByTutorId(tutorId, pageable);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findByIdAndWriter_Id(reviewId, userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        reviewRepository.delete(reviewEntity);
    }
}
