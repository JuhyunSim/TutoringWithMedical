package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.dto.filter.FilterRequestDto;
import com.simzoo.withmedical.dto.tuteePost.CreateTuteePostingRequestDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingResponseDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.tuteePost.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.GradeType;
import com.simzoo.withmedical.enums.TutoringType;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import com.simzoo.withmedical.service.TuteePostService;
import com.simzoo.withmedical.util.resolver.LoginId;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<TuteePostingResponseDto> createInquiryPosting(@LoginId Long memberId,
        @Valid @RequestBody CreateTuteePostingRequestDto requestDto) {

        return ResponseEntity.ok(
            tuteePostService.saveInquiryPosting(memberId, requestDto).toResponseDto());
    }

    /**
     * 게시물 수정
     */
    @PatchMapping("/posting/{postingId}")
    public ResponseEntity<TuteePostingResponseDto> updateInquiryPosting(@LoginId Long memberId,
        @PathVariable Long postingId,
        @RequestBody UpdateTuteePostingRequestDto requestDto) {

        return ResponseEntity.ok(
            tuteePostService.changeInquiryPosting(memberId, postingId, requestDto).toResponseDto());
    }

    /**
     * 게시물 개별 조회
     */
    @GetMapping("/posting/{postingId}")
    public ResponseEntity<TuteePostingResponseDto> getInquiryPosting(@PathVariable Long postingId) {

        return ResponseEntity.ok(tuteePostService.getInquiryPosting(postingId).toResponseDto());
    }

    /**
     * 게시물 전체조회
     */
    @GetMapping("/postings")
    public ResponseEntity<Page<TuteePostingSimpleResponseDto>> getPostings(
        @RequestParam(required = false) List<TuteePostSortCriteria> sortBy,
        @RequestParam(required = false) List<Direction> sortDirection,
        @RequestParam(required = false) Gender gender,
        @RequestParam(required = false) GradeType tuteeGradeType,
        @RequestParam(required = false) TutoringType tutoringType,
        @PageableDefault(page = 0, size = 10, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

        // sortBy와 direction을 SortRequestDto로 변환하여 처리
        int sortBySize = sortBy == null ? 0 : sortBy.size();

        List<SortRequestDto<TuteePostSortCriteria>> sortRequests = IntStream
            .range(0, sortBySize)
            .mapToObj(i -> SortRequestDto.<TuteePostSortCriteria>builder()
                .sortBy(sortBy.get(i))
                .direction(sortDirection.get(i))
                .build())
            .toList();

        FilterRequestDto filterRequests = FilterRequestDto.builder()
            .gender(gender)
            .tuteeGradeType(tuteeGradeType)
            .tutoringType(tutoringType)
            .build();

        return ResponseEntity.ok(
            tuteePostService.getInquiryPostings(pageable, sortRequests, filterRequests));
    }

    @GetMapping("/postings/me")
    public ResponseEntity<?> getMyPostings(@LoginId Long myId,
        @PageableDefault(page = 0, size = 10, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(tuteePostService.getMyPostings(myId, pageable)
            .map(TuteePostEntity::toSimpleResponseDto));
    }

    @DeleteMapping("/postings/{postingId}")
    public ResponseEntity<?> deleteInquiryPosting(@LoginId Long memberId,
        @PathVariable Long postingId) {

        tuteePostService.deleteInquiryPosting(memberId, postingId);

        return ResponseEntity.ok().build();
    }
}
