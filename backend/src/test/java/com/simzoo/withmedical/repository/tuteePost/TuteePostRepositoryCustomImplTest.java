package com.simzoo.withmedical.repository.tuteePost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.dto.filter.FilterRequestDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.GradeType;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TuteePostRepositoryCustomImplTest {

    @Autowired
    private TuteePostRepository tuteePostRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;

    @Test
    @DisplayName("필터링 적용 테스트")
    @Transactional
    void findAllTuteePostingsTest() {
        //given
        //조회 조건
        int pageSize = 10;
        int pageNumber = 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<SortRequestDto<TuteePostSortCriteria>> sortRequests = Arrays.asList(
            SortRequestDto.<TuteePostSortCriteria>builder()
                .build(),
            SortRequestDto.<TuteePostSortCriteria>builder()
                .build(),
            SortRequestDto.<TuteePostSortCriteria>builder()
                .build()
        );

        FilterRequestDto filterRequest = FilterRequestDto.builder()
            .tuteeGradeType(GradeType.ELEMENTARY)
            .tutoringType(TutoringType.OFF_LINE)
            .gender(Gender.MALE)
            .build();

        //데이터 저장
        MemberEntity member = MemberEntity.builder()
            .tuteeProfiles(new ArrayList<>())
            .nickname("nickname")
            .gender(Gender.MALE)
            .roles(List.of(Role.TUTEE))
            .build();

        MemberEntity member2 = MemberEntity.builder()
            .tuteeProfiles(new ArrayList<>())
            .nickname("nickname2")
            .gender(Gender.MALE)
            .roles(List.of(Role.TUTEE))
            .build();

        List<MemberEntity> memberList = List.of(member, member2);

        memberRepository.saveAll(memberList);

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
            .location(Location.SEOUL)
            .build();

        TuteeProfileEntity tuteeProfile2 = TuteeProfileEntity.builder()
            .member(member2)
            .grade(TuteeGrade.ELEMENTARY_1)
            .gradeGroup(GradeType.ELEMENTARY)
            .name("name2")
            .posts(new ArrayList<>())
            .subjects(new ArrayList<>())
            .gender(Gender.MALE)
            .description("tutee_description2")
            .personality("personality2")
            .school("school2")
            .location(Location.INCHEON)
            .build();

        List<TuteeProfileEntity> tuteeProfileList = List.of(tuteeProfile, tuteeProfile2);

        tuteeProfileRepository.saveAll(tuteeProfileList);

        TuteePostEntity tuteePost = TuteePostEntity.builder()
            .tuteeProfile(tuteeProfile)
            .description("description")
            .type(TutoringType.OFF_LINE)
            .level("상")
            .fee(500000)
            .possibleSchedule("any")
            .build();

        TuteePostEntity tuteePost2 = TuteePostEntity.builder()
            .tuteeProfile(tuteeProfile2)
            .description("description2")
            .type(TutoringType.OFF_LINE)
            .level("하")
            .fee(300000)
            .possibleSchedule("any")
            .build();

        List<TuteePostEntity> tuteePostList = List.of(tuteePost, tuteePost2);

        tuteePostRepository.saveAll(tuteePostList);

        // when
        Page<TuteePostingSimpleResponseDto> results = tuteePostRepository.findAllTuteePostings(
            pageable, sortRequests, filterRequest);

        //then
        assertNotNull(results);
        assertEquals(1, results.getTotalElements());
    }

    @Test
    @DisplayName("정렬 적용 테스트")
    @Transactional
    void findAllTuteePostingsTest_sorting() {
        //given
        //조회 조건
        int pageSize = 10;
        int pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<SortRequestDto<TuteePostSortCriteria>> sortRequests = Arrays.asList(
            SortRequestDto.<TuteePostSortCriteria>builder()
                .sortBy(TuteePostSortCriteria.CREATED_AT)
                .direction(Direction.DESC)
                .build(),
            SortRequestDto.<TuteePostSortCriteria>builder()
                .sortBy(TuteePostSortCriteria.FEE)
                .direction(Direction.DESC)
                .build(),
            SortRequestDto.<TuteePostSortCriteria>builder()
                .sortBy(TuteePostSortCriteria.TUTEE_GRADE)
                .direction(Direction.ASC)
                .build()
        );

        FilterRequestDto filterRequest = FilterRequestDto.builder()
            .build();

        //데이터 저장
        MemberEntity member = MemberEntity.builder()
            .tuteeProfiles(new ArrayList<>())
            .nickname("nickname")
            .gender(Gender.MALE)
            .roles(List.of(Role.TUTEE))
            .build();

        MemberEntity member2 = MemberEntity.builder()
            .tuteeProfiles(new ArrayList<>())
            .nickname("nickname2")
            .gender(Gender.MALE)
            .roles(List.of(Role.TUTEE))
            .build();

        List<MemberEntity> memberList = List.of(member, member2);

        memberRepository.saveAll(memberList);

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
            .location(Location.SEOUL)
            .build();

        member.saveTuteeProfiles(List.of(tuteeProfile), Role.TUTEE);

        TuteeProfileEntity tuteeProfile2 = TuteeProfileEntity.builder()
            .member(member2)
            .grade(TuteeGrade.ELEMENTARY_1)
            .gradeGroup(GradeType.ELEMENTARY)
            .name("name2")
            .posts(new ArrayList<>())
            .subjects(new ArrayList<>())
            .gender(Gender.MALE)
            .description("tutee_description2")
            .personality("personality2")
            .school("school2")
            .location(Location.INCHEON)
            .build();

        member2.saveTuteeProfiles(List.of(tuteeProfile2), Role.TUTEE);

        List<TuteeProfileEntity> tuteeProfileList = List.of(tuteeProfile, tuteeProfile2);

        tuteeProfileRepository.saveAll(tuteeProfileList);

        TuteePostEntity tuteePost = TuteePostEntity.builder()
            .tuteeProfile(tuteeProfile)
            .description("description")
            .type(TutoringType.OFF_LINE)
            .level("상")
            .fee(500000)
            .possibleSchedule("any")
            .build();

        tuteeProfile.addPost(tuteePost);

        TuteePostEntity tuteePost2 = TuteePostEntity.builder()
            .tuteeProfile(tuteeProfile2)
            .description("description2")
            .type(TutoringType.OFF_LINE)
            .level("하")
            .fee(300000)
            .possibleSchedule("any")
            .build();

        tuteeProfile.addPost(tuteePost2);

        List<TuteePostEntity> tuteePostList = List.of(tuteePost, tuteePost2);

        tuteePostRepository.saveAll(tuteePostList);

        // when
        Page<TuteePostingSimpleResponseDto> results = tuteePostRepository.findAllTuteePostings(
            pageable, sortRequests, filterRequest);

        //then
        assertNotNull(results);
        assertEquals(2, results.getTotalElements());
        assertEquals(2, results.getContent().size());
        assertEquals(TuteeGrade.HIGH_1, results.getContent().get(0).getStudentGrade());
    }
}