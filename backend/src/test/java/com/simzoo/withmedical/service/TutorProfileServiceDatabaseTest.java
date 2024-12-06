package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import({QueryDslConfig.class, TutorProfileService.class, ObjectMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TutorProfileServiceDatabaseTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private TutorProfileService tutorProfileService;

    @Test
    void testFindAllTutorProfiles() {
        //저장
        MemberEntity member1 = MemberEntity.builder()
            .nickname("nickname1")
            .gender(Gender.MALE)
            .tutorProfile(null)
            .roles(List.of(Role.TUTOR))
            .build();

        MemberEntity member2 = MemberEntity.builder()
            .nickname("nickname2")
            .gender(Gender.FEMALE)
            .tutorProfile(null)
            .roles(List.of(Role.TUTOR))
            .build();

        List<MemberEntity> memberList = List.of(member1, member2);
        memberRepository.saveAll(memberList);

        TutorProfileEntity tutor1 = TutorProfileEntity.builder()
            .member(member1)
            .location(Location.SEOUL)
            .university(University.KOREA_UNIVERSITY)
            .status(EnrollmentStatus.ENROLLED)
            .subjects(new ArrayList<>())
            .build();

        member1.saveTutorProfile(tutor1, Role.TUTOR);

        TutorProfileEntity tutor2 = TutorProfileEntity.builder()
            .member(member2)
            .location(Location.INCHEON)
            .university(University.KOREA_UNIVERSITY)
            .status(EnrollmentStatus.ENROLLED)
            .subjects(new ArrayList<>())
            .build();

        member2.saveTutorProfile(tutor2, Role.TUTOR);

        tutorProfileRepository.saveAll(List.of(tutor1, tutor2));

        List<SubjectEntity> subjectEntities1 = List.of(
            SubjectEntity.builder().tutorProfile(tutor1).subject(Subject.ELEMENTARY_ENGLISH)
                .build(),
            SubjectEntity.builder().tutorProfile(tutor1).subject(Subject.MIDDLE_ENGLISH).build()
        );

        List<SubjectEntity> subjectEntities2 = List.of(SubjectEntity.builder().tutorProfile(tutor2)
            .subject(Subject.MIDDLE_ENGLISH).build());

        subjectRepository.saveAll(subjectEntities1);
        subjectRepository.saveAll(subjectEntities2);

        tutor1.addSubjects(subjectEntities1);
        tutor2.addSubjects(subjectEntities2);

        //given
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "createdAt");
        TutorFilterRequestDto filterRequest = TutorFilterRequestDto.builder()
            .gender(Gender.MALE)
            .subjects(List.of(Subject.ELEMENTARY_ENGLISH, Subject.MIDDLE_ENGLISH))
            .build();

        //when
        Page<TutorSimpleResponseDto> tutorProfileDtos = tutorProfileService.getTutorList(pageable,
            filterRequest);

        //then
        assertEquals(2, tutorProfileDtos.getTotalElements());
    }
}