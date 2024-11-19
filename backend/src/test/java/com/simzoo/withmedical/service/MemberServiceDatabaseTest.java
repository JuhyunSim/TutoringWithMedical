package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({MemberService.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class  MemberServiceDatabaseTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Test
    @DisplayName("학생프로필 저장 확인")
    @Transactional
    void addTuteeProfile_profileSavedInDatabase() {
        //given

        Role role = Role.PARENT;

        TuteeProfileRequestDto requestDto = TuteeProfileRequestDto.builder()
            .tuteeGrade(TuteeGrade.HIGH_1)
            .tuteeName("testName")
            .gender(Gender.MALE)
            .description("testDescription")
            .subjects(List.of(Subject.HIGH_ENGLISH))
            .build();

        MemberEntity parent = MemberEntity.builder()
            .nickname("parent")
            .gender(Gender.FEMALE)
            .roles(List.of(role))
            .build();

        MemberEntity parentEntity = memberRepository.save(parent);

        //when
        MemberEntity result = memberService.addTuteeProfile(parentEntity.getId(), role,
            requestDto);

        //then
        assertEquals(1, result.getTuteeProfiles().size());
        assertEquals("testName", result.getTuteeProfiles().get(0).getName());
        assertEquals("parent", result.getNickname());
    }

    @Test
    @DisplayName("학생프로필 조회 확인")
    @Transactional
    void getTuteeProfile_profileSavedInDatabase() {
        //given

        Role role = Role.PARENT;

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .name("tuteeName")
            .gender(Gender.FEMALE)
            .description("testDescription")
            .subjects(new ArrayList<>())
            .build();

        tuteeProfileRepository.save(tuteeProfile);

        List<SubjectEntity> subjects = List.of(
            SubjectEntity.of(Subject.ELEMENTARY_ENGLISH, tuteeProfile));
        tuteeProfile.addSubject(subjects);

        subjectRepository.saveAll(subjects);

        MemberEntity parent = MemberEntity.builder()
            .nickname("parent")
            .gender(Gender.FEMALE)
            .roles(List.of(role))
            .build();

        MemberEntity parentEntity = memberRepository.save(parent);
        parentEntity.saveTuteeProfiles(List.of(tuteeProfile), role);
        //when
        MemberEntity result = memberService.getMyInfo(parentEntity.getId());

        //then
        assertEquals(1, result.getTuteeProfiles().get(0).getSubjects().size());
        assertEquals("parent", result.getNickname());
    }

    @Test
    @DisplayName("학부모의 학생 프로필 저장 확인")
    @Transactional
    void updateParentTuteeProfile_profileSavedInDatabase() {
        //given
        Role role = Role.PARENT;

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .name("tuteeName")
            .gender(Gender.FEMALE)
            .description("testDescription")
            .subjects(new ArrayList<>())
            .build();

        TuteeProfileEntity savedTutee = tuteeProfileRepository.save(tuteeProfile);

        List<SubjectEntity> subjects = List.of(
            SubjectEntity.of(Subject.ELEMENTARY_ENGLISH, savedTutee));

        List<SubjectEntity> savedSubjects = subjectRepository.saveAll(subjects);

        tuteeProfile.addSubject(savedSubjects);

        MemberEntity parent = MemberEntity.builder()
            .nickname("parent")
            .gender(Gender.FEMALE)
            .roles(List.of(role))
            .build();

        MemberEntity parentEntity = memberRepository.save(parent);
        parentEntity.saveTuteeProfiles(List.of(savedTutee), role);

        List<Subject> newSubjects = List.of(Subject.HIGH_ENGLISH);

        UpdateMemberRequestDto requestDto = UpdateMemberRequestDto.builder()
            .nickname("updatedNickname")
            .tuteeProfile(UpdateTuteeProfileRequestDto.builder()
                .tuteeName("updatedTuteeName")
                .tuteeId(savedTutee.getId())
                .subjects(newSubjects)
                .build())
            .build();

        //when
        MemberEntity result = memberService.updateParentProfile(parentEntity.getId(), requestDto);

        //then
        assertEquals("updatedNickname", result.getNickname());
        assertEquals(Subject.HIGH_ENGLISH,
            result.getTuteeProfiles().get(0).getSubjects().get(0).getSubject());
        assertEquals("updatedTuteeName", result.getTuteeProfiles().get(0).getName());
    }

    @Test
    @DisplayName("회원삭제 테스트_학생 회원")
    @Transactional
    void cancleAccount() {
        //given
        Role role = Role.TUTEE;

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
            .build();

        MemberEntity savedMember = memberRepository.save(member);
        savedMember.saveTuteeProfiles(List.of(savedTutee), role);

        //when
         memberService.deleteMember(savedMember.getId());

        //then
        assertTrue(memberRepository.findById(savedMember.getId()).isEmpty());
        assertTrue(tuteeProfileRepository.findAll().isEmpty());
        assertTrue(subjectRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("회원삭제 테스트_선생님 회원")
    @Transactional
    void cancleAccount_Tutor() {
        //given
        Role role = Role.TUTOR;

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
            .build();

        MemberEntity savedMember = memberRepository.save(member);
        savedMember.saveTutorProfile(savedTutor, role);

        //when
        memberService.deleteMember(savedMember.getId());

        //then
        assertTrue(memberRepository.findById(savedMember.getId()).isEmpty());
        assertTrue(tutorProfileRepository.findAll().isEmpty());
        assertTrue(subjectRepository.findAll().isEmpty());
    }

}