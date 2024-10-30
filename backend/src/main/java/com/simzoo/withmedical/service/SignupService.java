package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.repository.MemberRepository;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.TutorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final TutorProfileRepository tutorProfileRepository;
    private final MemberRepository memberRepository;
    private final TuteeProfileRepository tuteeProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberEntity signup(SignupRequestDto requestDto) {

        MemberEntity member = memberRepository.save(requestDto.toMemberEntity(passwordEncoder));

        if (requestDto.getRole() == Role.TUTEE) {
            member.saveTuteeProfile(
                tuteeProfileRepository.save(requestDto.getTuteeProfile().toEntity(member)),
                requestDto.getRole());
        } else if (requestDto.getRole() == Role.TUTOR) {
            member.saveTutorProfile(
                tutorProfileRepository.save(requestDto.getTutorProfile().toEntity(member)),
                requestDto.getRole());
        } else {
            tuteeProfileRepository.saveAll(
                requestDto.getTuteeProfiles().stream().map(e -> e.toEntity(member)).toList());
        }
        return member;
    }
}
