package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileJdbcRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({TutorProfileService.class, TutorProfileJdbcRepository.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TutorProfileServiceDatabaseTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private TutorProfileService tutorProfileService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            .gender(Gender.MALE)
            .tutorProfile(null)
            .roles(List.of(Role.TUTOR))
            .build();

        List<MemberEntity> memberList = List.of(member1, member2);
        memberRepository.saveAll(memberList);

        TutorProfileEntity tutor1 = TutorProfileEntity.builder()
            .member(member1)
            .location("서울특별시 강남구")
            .univName("고려대학교")
            .status(EnrollmentStatus.ENROLLED)
            .subjects(new ArrayList<>())
            .build();

        member1.saveTutorProfile(tutor1, Role.TUTOR);

        TutorProfileEntity tutor2 = TutorProfileEntity.builder()
            .member(member2)
            .location("인천광역시 연수구")
            .univName("고려대학교")
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

        System.out.println("tutor1 subjects = " + tutorProfileRepository.findById(tutor1.getId()).get().getSubjects());
        System.out.println("tutor2 subjects = " + tutorProfileRepository.findById(tutor2.getId()).get().getSubjects());

        //Debug
        // LEFT JOIN 결과 확인 쿼리
        java.lang.String debugSql = """
            SELECT 
                tpe.id AS tutor_id,
                m.nickname AS member_nickname,
                ARRAY_AGG(s.subject) AS aggregated_subjects
            FROM tutorProfile tpe
            LEFT JOIN member m ON tpe.memberId = m.id
            LEFT JOIN SubjectEntity s ON tpe.id = s.tutorId
            GROUP BY tpe.id, m.nickname
        """;

        //TutorSimpleResponseDto 매핑
        RowMapper<TutorSimpleResponseDto> rowMapper = (rs, rowNum) -> {
            List<java.lang.String> subjectList = Arrays.asList((java.lang.String[]) rs.getArray("subjects").getArray());
            System.out.println("Mapped subjects: " + subjectList);
            return new TutorSimpleResponseDto(
                rs.getLong("tutor_id"),
                rs.getString("image_url"),
                rs.getString("tutor_nickname"),
                rs.getString("tutor_university"),
                rs.getString("tutor_location"),
                subjectList.stream().map(Subject::valueOf).toList()
            );
        };


        List<Map<java.lang.String, Object>> results = jdbcTemplate.queryForList(debugSql);
        results.forEach(result -> {
            System.out.println("Result Row: " + result);
        });

        //given
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "createdAt");
        TutorFilterRequestDto.TutorEnumFilter filterRequest = TutorFilterRequestDto.TutorEnumFilter.builder()
//            .gender(Gender.MALE)
//            .subjects(List.of(Subject.ELEMENTARY_ENGLISH, Subject.MIDDLE_ENGLISH))
//            .locations(new ArrayList<>())
//            .universities(new ArrayList<>())
//            .statusList(new ArrayList<>())
            .gender(null)
            .subjects(null)
            .locations(null)
            .universities(null)
            .statusList(null)
            .build();


        //when
        Page<TutorSimpleResponseDto> tutorProfileDtos = tutorProfileService.getTutorList(pageable,
            filterRequest);

        //then
        assertEquals(2, tutorProfileDtos.getTotalElements());
    }
}