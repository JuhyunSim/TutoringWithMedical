package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.auth.JwtResponseDto;
import com.simzoo.withmedical.dto.auth.LoginRequestDto;
import com.simzoo.withmedical.dto.member.ChangePasswordDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import com.simzoo.withmedical.util.AesUtil;
import com.simzoo.withmedical.util.JwtUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubjectRepository subjectRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final TuteeProfileRepository tuteeProfileRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public JwtResponseDto login(LoginRequestDto requestDto) {

        String phoneNumber = requestDto.getPhoneNumber();
        String password = requestDto.getPassword();

        MemberEntity memberEntity = memberRepository.findByHashedPhoneNumber(
                AesUtil.generateHash(phoneNumber))
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (notMatchPassword(memberEntity, password, passwordEncoder)) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        memberEntity.updateLastLogin(LocalDateTime.now());

        return JwtResponseDto.builder().accessToken(
            jwtUtil.generateAccessToken(memberEntity.getNickname(), memberEntity.getId(),
                requestDto.getRole())).build();
    }

    @Transactional
    public void changeMyPassword(Long userId, ChangePasswordDto requestDto) {

        MemberEntity memberEntity = memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (notMatchPassword(memberEntity, requestDto.getOldPassword(), passwordEncoder)) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        memberEntity.changePassword(requestDto.getNewPassword(), passwordEncoder);
    }

    @Transactional
    public void deleteMember(Long userId, String password) {
        MemberEntity memberEntity = memberRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (notMatchPassword(memberEntity, password, passwordEncoder)) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        List<TuteeProfileEntity> tuteeProfiles = memberEntity.getTuteeProfiles();
        TutorProfileEntity tutorProfile = memberEntity.getTutorProfile();

        // 1. SubjectEntity 삭제
        List<SubjectEntity> subjectsToDelete = new ArrayList<>();
        Optional.ofNullable(tuteeProfiles).ifPresent(profiles ->
            profiles.forEach(e -> subjectsToDelete.addAll(e.getSubjects()))
        );
        Optional.ofNullable(tutorProfile).ifPresent(profile ->
            subjectsToDelete.addAll(profile.getSubjects())
        );
        subjectRepository.deleteAll(subjectsToDelete);

        // 2. TuteeProfileEntity 삭제
        Optional.ofNullable(tuteeProfiles).ifPresent(tuteeProfileRepository::deleteAll);

        // 3. TutorProfileEntity 삭제
        Optional.ofNullable(tutorProfile).ifPresent(tutorProfileRepository::delete);

        // 4. MemberEntity 삭제
        memberRepository.delete(memberEntity);
    }


    private static boolean notMatchPassword(MemberEntity member, String password,
        PasswordEncoder passwordEncoder) {
        return !passwordEncoder.matches(password, member.getPassword());
    }
}
