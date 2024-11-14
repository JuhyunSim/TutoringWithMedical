package com.simzoo.withmedical.repository.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryCustomImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;

    @Test
    @Transactional
    void findParentWithTuteeProfiles() {
        //given
        MemberEntity member = MemberEntity.builder()
            .nickname("testNickname")
            .roles(List.of(Role.PARENT))
            .build();

        MemberEntity memberEntity = memberRepository.save(member);

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .member(memberEntity)
            .grade(TuteeGrade.HIGH_1)
            .description("test")
            .build();

        memberEntity.saveTuteeProfiles(List.of(tuteeProfile), Role.PARENT);

        tuteeProfileRepository.save(tuteeProfile);

        //when
        Optional<MemberEntity> result = memberRepository.findParentWithTuteeProfile(member.getId());
        //then
        assertTrue(result.isPresent());
        assertEquals("testNickname", result.get().getNickname());
        assertEquals(1, result.get().getTuteeProfiles().size());
    }
}