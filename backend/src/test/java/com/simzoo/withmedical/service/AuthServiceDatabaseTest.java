package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.simzoo.withmedical.TestSecurityConfig;
import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({AuthService.class, QueryDslConfig.class, TestSecurityConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthServiceDatabaseTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Qualifier("passwordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원삭제 테스트_학생 회원")
    @Transactional
    void cancelAccount() {
        //given
        Role role = Role.TUTEE;

        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .name("tuteeName")
            .gender(Gender.FEMALE)
            .description("testDescription")
            .subjects(new ArrayList<>())
            .build();

        TuteeProfileEntity savedTutee = tuteeProfileRepository.save(tuteeProfile);

        SubjectEntity subject = SubjectEntity.builder()
            .tuteeProfile(savedTutee)
            .subject(Subject.ELEMENTARY_ENGLISH)
            .build();

        SubjectEntity savedSubject = subjectRepository.save(subject);

        savedTutee.addSubject(List.of(savedSubject));

        MemberEntity member = MemberEntity.builder()
            .nickname("parent")
            .gender(Gender.FEMALE)
            .roles(List.of(role))
            .password(encodedPassword)
            .build();

        MemberEntity savedMember = memberRepository.save(member);
        savedMember.saveTuteeProfiles(List.of(savedTutee), role);

        //when
        authService.deleteMember(savedMember.getId(), password);

        //then
        assertTrue(memberRepository.findById(savedMember.getId()).isEmpty());
        assertTrue(tuteeProfileRepository.findAll().isEmpty());
        assertTrue(subjectRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("회원삭제 테스트_선생님 회원")
    @Transactional
    void cancelAccount_Tutor() {
        //given
        Role role = Role.TUTOR;

        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        TutorProfileEntity tutorProfile = TutorProfileEntity.builder()
            .imageUrl("testImage")
            .description("testDescription")
            .subjects(new ArrayList<>())
            .build();

        TutorProfileEntity savedTutor = tutorProfileRepository.save(tutorProfile);

        SubjectEntity subject = SubjectEntity.builder()
            .tutorProfile(savedTutor)
            .subject(Subject.ELEMENTARY_ENGLISH)
            .build();

        SubjectEntity savedSubject = subjectRepository.save(subject);

        savedTutor.addSubjects(List.of(savedSubject));

        MemberEntity member = MemberEntity.builder()
            .nickname("parent")
            .gender(Gender.FEMALE)
            .roles(List.of(role))
            .password(encodedPassword)
            .build();

        MemberEntity savedMember = memberRepository.save(member);
        savedMember.saveTutorProfile(savedTutor, role);

        //when
        authService.deleteMember(savedMember.getId(), password);

        //then
        assertTrue(memberRepository.findById(savedMember.getId()).isEmpty());
        assertTrue(tutorProfileRepository.findAll().isEmpty());
        assertTrue(subjectRepository.findAll().isEmpty());
    }

}