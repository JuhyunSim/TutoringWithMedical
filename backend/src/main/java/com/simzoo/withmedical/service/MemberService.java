package com.simzoo.withmedical.service;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public void checkMemberExist(String phoneNumber) {
        memberRepository.findByPhoneNumber(phoneNumber).ifPresent(memberEntity -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        });
    }
}
