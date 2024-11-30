package com.simzoo.withmedical.config;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tuteePost.TuteePostRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataInit {

    @Bean
    CommandLineRunner loadTestData(MemberRepository memberRepository,
        TuteeProfileRepository tuteeProfileRepository,
        TutorProfileRepository tutorProfileRepository,
        TuteePostRepository tuteePostRepository,
        SubjectRepository subjectRepository) {
        return args -> {
            // Members 생성
            for (int i = 1; i <= 100; i++) {
                MemberEntity member = MemberEntity.builder()
                    .nickname("member" + i)
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .roles(List.of(Role.TUTEE))
                    .build();
                memberRepository.save(member);

                // TuteeProfiles 생성
                TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
                    .member(member)
                    .name("Tutee" + i)
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .grade(i % 3 == 0 ? TuteeGrade.ELEMENTARY_1 : TuteeGrade.HIGH_1)
                    .location(i % 5 == 0 ? Location.SEOUL : Location.INCHEON)
                    .school("School" + i)
                    .personality("Personality" + i)
                    .description("Tutee description " + i)
                    .build();

                tuteeProfile.syncGradeGroup();

                tuteeProfileRepository.save(tuteeProfile);

                // TuteePosts 생성
                for (int j = 1; j <= 5; j++) {
                    TuteePostEntity tuteePost = TuteePostEntity.builder()
                        .tuteeProfile(tuteeProfile)
                        .description("Post Description " + j)
                        .type(j % 2 == 0 ? TutoringType.OFF_LINE : TutoringType.ON_LINE)
                        .possibleSchedule("Schedule" + j)
                        .level("Level" + j)
                        .fee(j * 10000)
                        .build();
                    tuteePostRepository.save(tuteePost);
                }

                // Subjects 생성
                for (Subject subject : Subject.values()) {
                    SubjectEntity subjectEntity = SubjectEntity.of(subject, tuteeProfile);
                    subjectRepository.save(subjectEntity);
                }
            }

            // TutorProfiles 및 관련 데이터 생성
            for (int i = 1; i <= 50; i++) {
                MemberEntity tutorMember = MemberEntity.builder()
                    .nickname("tutor" + i)
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .roles(List.of(Role.TUTOR))
                    .build();
                memberRepository.save(tutorMember);

                TutorProfileEntity tutorProfile = TutorProfileEntity.builder()
                    .memberId(tutorMember.getId())
                    .location(Location.SEOUL)
                    .university(i % 2 == 0 ? University.KOREA_UNIVERSITY : University.YONSEI_UNIVERSITY)
                    .status(EnrollmentStatus.ENROLLED)
                    .description("Tutor Description " + i)
                    .build();
                tutorProfileRepository.save(tutorProfile);

                for (Subject subject : Subject.values()) {
                    SubjectEntity subjectEntity = SubjectEntity.of(subject, tutorProfile);
                    subjectRepository.save(subjectEntity);
                }
            }
        };
    }
}

