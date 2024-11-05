package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.repository.MemberRepository;
import com.simzoo.withmedical.repository.SubjectRepository;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import com.simzoo.withmedical.util.AesUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    TutorProfileRepository tutorProfileRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    TuteeProfileRepository tuteeProfileRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    SubjectRepository subjectRepository;

    @InjectMocks
    SignupService signupService;

    @Test
    void signup_tutor_success() {
        //given
        TutorProfileRequestDto tutorRequest = TutorProfileRequestDto.builder()
            .proofFile(null)
            .profileImage(null)
            .subjects(List.of(Subject.HIGH_ENGLISH, Subject.MIDDLE_ENGLISH))
            .location(Location.BUSAN)
            .description("description")
            .university(University.KOREA_UNIVERSITY)
            .status(EnrollmentStatus.ENROLLED)
            .build();

        SignupRequestDto requestDto = SignupRequestDto.builder()
            .nickname("nickname")
            .gender(Gender.MALE)
            .phoneNumber("phoneNumber")
            .password("password")
            .passwordConfirm("passwordConfirm")
            .role(Role.TUTOR)
            .tutorProfile(tutorRequest)
            .build();

        MemberEntity member = MemberEntity.builder()
            .id(1L)
            .nickname(requestDto.getNickname())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .gender(requestDto.getGender())
            .phoneNumber(AesUtil.generateHash(requestDto.getPhoneNumber()))
            .roles(new ArrayList<>())
            .build();

        TutorProfileEntity tutorProfile = TutorProfileEntity.builder()
            .location(Location.BUSAN)
            .description("description")
            .university(University.KOREA_UNIVERSITY)
            .status(EnrollmentStatus.ENROLLED)
            .member(member)
            .subjects(new ArrayList<>())
            .build();

        List<SubjectEntity> subjectEntities = tutorRequest.getSubjects().stream()
            .map(e -> SubjectEntity.of(e, tutorProfile)).toList();

        when(memberRepository.save(argThat(e ->
            e.getNickname().equals(member.getNickname()) &&
                e.getGender().equals(member.getGender()) &&
                e.getPhoneNumber().equals(member.getPhoneNumber())
        ))).thenReturn(member);

        when(tutorProfileRepository.save(argThat(e ->
            e.getMember().equals(member) &&
                e.getLocation().equals(tutorProfile.getLocation()) &&
                e.getUniversity().equals(tutorProfile.getUniversity()) &&
                e.getStatus().equals(tutorProfile.getStatus()) &&
                e.getDescription().equals(tutorProfile.getDescription())))).thenReturn(
            tutorProfile);

        when(subjectRepository.saveAll(anyList())).thenReturn(
            subjectEntities);

        //when
        MemberEntity result = signupService.signup(requestDto);

        //then
        assertNotNull(result);
        assertEquals("description", result.getTutorProfile().getDescription());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals(University.KOREA_UNIVERSITY, result.getTutorProfile().getUniversity());
    }

}