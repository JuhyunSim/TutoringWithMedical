package com.simzoo.withmedical.repository.chat.room;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.chat.ChatRoomExistDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import com.simzoo.withmedical.entity.chat.ChatRoomMember;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class ChatRoomRepositoryCustomImplTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private TuteeProfileRepository tuteeProfileRepository;
    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Test
    @Transactional
    void findByTwoMember() {
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
            .roles(List.of(Role.TUTEE))
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

        TuteeProfileEntity tutee1 = TuteeProfileEntity.builder()
            .member(member2)
            .location("인천광역시 연수구")
            .gender(Gender.MALE)
            .subjects(new ArrayList<>())
            .build();

        member2.saveTuteeProfiles(List.of(tutee1), Role.TUTEE);

        tutorProfileRepository.save(tutor1);
        tuteeProfileRepository.save(tutee1);

        List<SubjectEntity> subjectEntities1 = List.of(
            SubjectEntity.builder().tutorProfile(tutor1).subject(Subject.ELEMENTARY_ENGLISH)
                .build(),
            SubjectEntity.builder().tutorProfile(tutor1).subject(Subject.MIDDLE_ENGLISH).build()
        );

        List<SubjectEntity> subjectEntities2 = List.of(SubjectEntity.builder().tuteeProfile(tutee1)
            .subject(Subject.MIDDLE_ENGLISH).build());

        subjectRepository.saveAll(subjectEntities1);
        subjectRepository.saveAll(subjectEntities2);

        tutor1.addSubjects(subjectEntities1);
        tutee1.addSubject(subjectEntities2);

        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
            .title("title")
            .build();

        chatRoomRepository.save(chatRoom);

        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
            .chatRoom(chatRoom)
            .member(member1)
            .build();

        ChatRoomMember chatRoomMember2 = ChatRoomMember.builder()
            .chatRoom(chatRoom)
            .member(member2)
            .build();

        chatRoomMemberRepository.save(chatRoomMember);
        chatRoomMemberRepository.save(chatRoomMember2);

        //when
        Optional<ChatRoomExistDto> result = chatRoomRepository.findByTwoMember(member1.getId(),
            member2.getId());

        //then
        assertNotNull(result);
    }
}