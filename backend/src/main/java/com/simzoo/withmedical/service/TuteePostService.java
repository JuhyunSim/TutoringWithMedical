package com.simzoo.withmedical.service;


import com.simzoo.withmedical.dto.CreateTuteePostingRequestDto;
import com.simzoo.withmedical.dto.TuteePostingResponseDto;
import com.simzoo.withmedical.dto.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.MemberRepository;
import com.simzoo.withmedical.repository.tuteePost.TuteePostRepository;
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
    public TuteePostingResponseDto saveInquiryPosting(Long memberId,
        CreateTuteePostingRequestDto requestDto) {

        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return tuteePostRepository.save(requestDto.toEntity(memberEntity)).toResponseDto();
    }

    /**
     * 과외요청 게시물 수정
     */
    @Transactional
    public TuteePostingResponseDto changeInquiryPosting(Long memberId, Long postingId,
        UpdateTuteePostingRequestDto requestDto) {

        TuteePostEntity tuteePostEntity = tuteePostRepository.findByIdAndMember_Id(postingId, memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        tuteePostEntity.update(requestDto);

        return tuteePostEntity.toResponseDto();
    }

    @Transactional(readOnly = true)
    public TuteePostingResponseDto getInquiryPosting(Long postingId) {

        TuteePostEntity tuteePostEntity = tuteePostRepository.findById(postingId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return tuteePostEntity.toResponseDto();
    }

    @Transactional(readOnly = true)
    public Page<TuteePostEntity> getInquiryPostings(Pageable pageable) {

        return tuteePostRepository.findAll(pageable);
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
