package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.tuteePost.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.GradeType;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.tuteePost.TuteePostRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({QueryDslConfig.class, TuteePostService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TuteePostServiceDatabaseTest {

    @Autowired
    private TuteePostService tuteePostService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;
    @Autowired
    private TuteePostRepository tuteePostRepository;

    @Test
    void updateTest() {
        //given

        UpdateTuteePostingRequestDto requestDto = UpdateTuteePostingRequestDto.builder()
            .tutoringType(TutoringType.OFF_LINE)
            .possibleSchedule("changed")
            .description("changedDescription")
            .fee(400000)
            .build();

        //데이터 저장
        MemberEntity member = MemberEntity.builder()
            .tuteeProfiles(new ArrayList<>())
            .nickname("nickname")
            .gender(Gender.MALE)
            .roles(List.of(Role.TUTEE))
            .build();

        memberRepository.save(member);

        TuteeProfileEntity tuteeProfile = TuteeProfileEntity.builder()
            .member(member)
            .grade(TuteeGrade.HIGH_1)
            .gradeGroup(GradeType.HIGH)
            .name("name")
            .posts(new ArrayList<>())
            .subjects(new ArrayList<>())
            .gender(Gender.MALE)
            .description("tutee_description")
            .personality("personality")
            .school("school")
            .location("서울특별시 강남구")
            .build();

        member.saveTuteeProfiles(List.of(tuteeProfile), Role.TUTEE);

        tuteeProfileRepository.save(tuteeProfile);

        TuteePostEntity tuteePost = TuteePostEntity.builder()
            .tuteeProfile(tuteeProfile)
            .description("description")
            .type(TutoringType.OFF_LINE)
            .level("상")
            .fee(500000)
            .possibleSchedule("any")
            .build();

        tuteeProfile.addPost(tuteePost);

        tuteePostRepository.save(tuteePost);

        //when
        TuteePostEntity result = tuteePostService.changeInquiryPosting(member.getId(),
            tuteePost.getId(), requestDto);

        TuteeProfileEntity updatedProfile = tuteeProfileRepository.findById(tuteeProfile.getId())
            .get();

        MemberEntity updatedMember = memberRepository.findById(member.getId()).get();

        //then
        assertEquals("changedDescription", result.getDescription());
        assertEquals("changedDescription", updatedProfile.getPosts().get(0).getDescription());
        assertEquals("changedDescription",
            updatedMember.getTuteeProfiles().get(0).getPosts().get(0).getDescription());
    }
}