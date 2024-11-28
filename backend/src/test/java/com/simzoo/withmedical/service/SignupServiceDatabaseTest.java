package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.simzoo.withmedical.TestSecurityConfig;
import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({QueryDslConfig.class, SignupService.class, TestSecurityConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SignupServiceDatabaseTest {

    @Autowired
    private SignupService signupService;

    @Test
    @DisplayName("저장된 과목객체의 tuteeId 확인")
    @Transactional
    void saveParentMember_CheckSubjectTuteeId() {
        //given

        TuteeProfileRequestDto tuteeRequest1 = TuteeProfileRequestDto.builder()
            .subjects(new ArrayList<>(List.of(Subject.ELEMENTARY_ENGLISH, Subject.MIDDLE_ENGLISH)))
            .tuteeName("testName")
            .build();

        TuteeProfileRequestDto tuteeRequest2 = TuteeProfileRequestDto.builder()
            .subjects(new ArrayList<>(List.of(Subject.ELEMENTARY_ENGLISH, Subject.MIDDLE_ENGLISH)))
            .tuteeName("testName")
            .build();

        SignupRequestDto requestDto = SignupRequestDto.builder()
            .nickname("testNickname")
            .gender(Gender.FEMALE)
            .phoneNumber("testPhoneNumber")
            .password("testPassword")
            .passwordConfirm("testPasswordConfirm")
            .role(Role.PARENT)
            .tuteeProfiles(List.of(tuteeRequest1, tuteeRequest2))
            .build();

        //when
        MemberEntity result = signupService.signup(requestDto);

        //then
        Long tuteeId1 = result.getTuteeProfiles().get(0).getSubjects().get(0).getTuteeProfile().getId();
        Long tuteeId2 = result.getTuteeProfiles().get(1).getSubjects().get(0).getTuteeProfile().getId();

        assertEquals(2 , result.getTuteeProfiles().size());
        assertNotEquals(tuteeId1, tuteeId2);
    }
}