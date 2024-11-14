package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({MemberService.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceDatabaseTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;

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

}