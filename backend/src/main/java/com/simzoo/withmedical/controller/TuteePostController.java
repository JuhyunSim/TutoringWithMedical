package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.CreateTuteePostingRequestDto;
import com.simzoo.withmedical.dto.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.service.TuteePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tutee")
public class TuteePostController {

    private final TuteePostService tuteePostService;

    /**
     * 게시물 생성
     */
    @PostMapping("/posting")
    public ResponseEntity<?> createInquiryPosting(Long memberId,
        @Valid @RequestBody CreateTuteePostingRequestDto requestDto) {

        return ResponseEntity.ok(tuteePostService.saveInquiryPosting(memberId, requestDto));
    }

    /**
     * 게시물 수정
     */
    @PatchMapping("/posting/{postingId}")
    public ResponseEntity<?> updateInquiryPosting(Long memberId, @PathVariable Long postingId,
        @RequestBody UpdateTuteePostingRequestDto requestDto) {

        return ResponseEntity.ok(
            tuteePostService.changeInquiryPosting(memberId, postingId, requestDto));
    }

    /**
     * 게시물 개별 조회
     */
    @GetMapping("/posting/{postingId}")
    public ResponseEntity<?> getInquiryPosting(@PathVariable Long postingId) {

        return ResponseEntity.ok(tuteePostService.getInquiryPosting(postingId));
    }

    /**
     * 게시물 전체조회
     */
    @GetMapping("/postings")
    public ResponseEntity<?> getPostings(
        @PageableDefault(page = 0, size = 10, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(tuteePostService.getInquiryPostings(pageable)
            .map(TuteePostEntity::toSimpleResponseDto));
    }

    @GetMapping("/postings/me")
    public ResponseEntity<?> getMyPostings(Long myId,
        @PageableDefault(page = 0, size = 10, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(tuteePostService.getMyPostings(myId, pageable)
            .map(TuteePostEntity::toSimpleResponseDto));
    }

    @DeleteMapping("/postings/{postingId}")
    public ResponseEntity<?> deleteInquiryPosting(Long memberId, @PathVariable Long postingId) {

        tuteePostService.deleteInquiryPosting(memberId, postingId);

        return ResponseEntity.ok().build();
    }
}
