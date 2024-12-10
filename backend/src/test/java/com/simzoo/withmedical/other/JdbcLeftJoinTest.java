package com.simzoo.withmedical.other;

import com.simzoo.withmedical.config.QueryDslConfig;
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
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcLeftJoinTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testLeftJoinResult() {
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

        System.out.println("tutor1 subjects = " + tutorProfileRepository.findById(tutor1.getId()).get().getSubjects());
        System.out.println("tutor2 subjects = " + tutorProfileRepository.findById(tutor2.getId()).get().getSubjects());

        // LEFT JOIN 결과 확인 쿼리
        String debugSql = """
            SELECT 
                tpe.id AS tutor_id,
                m.nickname AS member_nickname,
                ARRAY_AGG(s.subject) AS aggregated_subjects
            FROM tutorProfile tpe
            LEFT JOIN member m ON tpe.memberId = m.id
            LEFT JOIN SubjectEntity s ON tpe.id = s.tutorId
            GROUP BY tpe.id, m.nickname
        """;


        List<Map<String, Object>> results = jdbcTemplate.queryForList(debugSql);
        results.forEach(result -> {
            System.out.println("Result Row: " + result);
        });

    }
}
