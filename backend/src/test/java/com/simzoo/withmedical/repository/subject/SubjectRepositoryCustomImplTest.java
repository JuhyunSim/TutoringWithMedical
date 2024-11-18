package com.simzoo.withmedical.repository.subject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SubjectRepositoryCustomImplTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TuteeProfileRepository profileRepository;

    @Test
    void fetchTuteeSubjectsTest() {
        //given

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .name("test")
            .build();

        TuteeProfileEntity savedTuteeProfile = profileRepository.save(tuteeProfile);

        SubjectEntity subject = SubjectEntity.builder()
            .tuteeProfile(savedTuteeProfile)
            .subject(Subject.ELEMENTARY_ENGLISH)
            .build();
        subjectRepository.save(subject);
        //when
        List<SubjectEntity> result = subjectRepository.fetchTuteeSubjects(
            savedTuteeProfile.getId());
        //then
        assertEquals(Subject.ELEMENTARY_ENGLISH, result.get(0).getSubject());
    }

}