package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.ReviewRequestDto;
import com.simzoo.withmedical.dto.ReviewResponseDto;
import com.simzoo.withmedical.dto.UpdateReviewRequestDto;
import com.simzoo.withmedical.service.ReviewService;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@LoginId Long userId,
        @RequestBody ReviewRequestDto requestDto) {

        return ResponseEntity.ok(reviewService.saveReview(userId, requestDto).toResponseDto());
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
        @RequestBody UpdateReviewRequestDto requestDto) {

        return ResponseEntity.ok(reviewService.changeReview(reviewId, requestDto).toResponseDto());
    }

    /**
     * 특정 튜터의 리뷰 조회
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getTutorReviews(@RequestParam Long tutorId,
        @PageableDefault(direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(reviewService.getTutorReviews(tutorId, pageable));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping({"reviewID"})
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
