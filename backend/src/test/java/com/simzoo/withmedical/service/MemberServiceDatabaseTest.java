package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.location.LocationDto;
import com.simzoo.withmedical.dto.location.LocationDto.Sido;
import com.simzoo.withmedical.dto.location.LocationDto.Sigungu;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
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
class MemberServiceDatabaseTest {

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
            .location(LocationDto.builder()
                .sido(Sido.builder()
                    .addr_name("서울특별시")
                    .build())
                .sigungu(Sigungu.builder()
                    .addr_name("강남구")
                    .full_addr("서울특별시 강남구")
                    .build())
                .build())
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
                .location(LocationDto.builder()
                    .sido(Sido.builder().addr_name("서울특별시").build())
                    .sigungu(Sigungu.builder().addr_name("강남구").full_addr("서울특별시 강남구").build())
                    .build())
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
}