package com.simzoo.withmedical.service;


import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.dto.filter.FilterRequestDto;
import com.simzoo.withmedical.dto.tuteePost.CreateTuteePostingRequestDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.tuteePost.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.tuteePost.TuteePostRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TuteePostService {

    private final TuteePostRepository tuteePostRepository;
    private final MemberRepository memberRepository;

    /**
     * 과외요청 게시물 저장
     */
    @Transactional
    public TuteePostEntity saveInquiryPosting(Long memberId,
        CreateTuteePostingRequestDto requestDto) {

        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        TuteePostEntity postEntity = requestDto.toEntity(memberEntity);

        if (memberEntity.getTuteeProfiles() == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PROFILE);
        }

        Map<Long, TuteeProfileEntity> tuteeMap = memberEntity.getTuteeProfiles().stream()
            .collect(Collectors.toMap(TuteeProfileEntity::getId, e -> e));

        postEntity.saveProfileInfo(tuteeMap.get(requestDto.getTuteeId()));

        return tuteePostRepository.save(postEntity);
    }

    /**
     * 과외요청 게시물 수정
     */
    @Transactional
    public TuteePostEntity changeInquiryPosting(Long memberId, Long postingId,
        UpdateTuteePostingRequestDto requestDto) {

        TuteePostEntity tuteePostEntity = tuteePostRepository.findByIdAndMember_Id(postingId,
                memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        tuteePostEntity.update(requestDto);

        return tuteePostEntity;
    }

    /**
     * 과외요청 게시물 단건 조회
     */
    @Transactional(readOnly = true)
    public TuteePostEntity getInquiryPosting(Long postingId) {

        return tuteePostRepository.findById(postingId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * 과외요청 게시물 전체조회
     */
    @Transactional(readOnly = true)
    public Page<TuteePostingSimpleResponseDto> getInquiryPostings(
        Pageable pageable, List<SortRequestDto<TuteePostSortCriteria>> sortRequests,
        FilterRequestDto filterRequestDto) {

        return tuteePostRepository.findAllTuteePostings(pageable, sortRequests, filterRequestDto);
    }

    @Transactional(readOnly = true)
    public Page<TuteePostEntity> getMyPostings(Long myId, Pageable pageable) {
        return tuteePostRepository.findAllByMember_Id(myId, pageable);
    }

    @Transactional
    public void deleteInquiryPosting(Long memberId, Long postingId) {

        TuteePostEntity tuteePostEntity = tuteePostRepository.findByIdAndMember_Id(postingId,
            memberId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        tuteePostRepository.delete(tuteePostEntity);

    }
}
