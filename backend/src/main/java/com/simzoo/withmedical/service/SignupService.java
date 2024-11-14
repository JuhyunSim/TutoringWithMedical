package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.tutor.TutorProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final TutorProfileRepository tutorProfileRepository;
    private final MemberRepository memberRepository;
    private final TuteeProfileRepository tuteeProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubjectRepository subjectRepository;

    @Transactional
    public MemberEntity signup(SignupRequestDto requestDto) {

        MemberEntity member = memberRepository.save(requestDto.toMemberEntity(passwordEncoder));
        member.addRole(requestDto.getRole());

        if (requestDto.getRole() == Role.TUTEE) {
            TuteeProfileEntity tuteeProfile = tuteeProfileRepository.save(
                requestDto.getTuteeProfile().toEntity(member));

            List<SubjectEntity> subjectEntities = saveTuteeSubjects(
                requestDto.getTuteeProfile().getSubjects(), tuteeProfile);

            tuteeProfile.addSubject(subjectEntities);

            member.saveTuteeProfiles(List.of(tuteeProfile), requestDto.getRole());
        } else if (requestDto.getRole() == Role.TUTOR) {
            TutorProfileEntity tutorProfile = tutorProfileRepository.save(
                requestDto.getTutorProfile().toEntity(member));

            List<SubjectEntity> subjectEntities = saveTutorSubjects(
                requestDto.getTutorProfile().getSubjects(), tutorProfile);

            tutorProfile.addSubjects(subjectEntities);

            member.saveTutorProfile(tutorProfile, requestDto.getRole());

        } else {
            List<TuteeProfileEntity> tuteeProfileList = requestDto.getTuteeProfiles().stream().map(e -> {
                TuteeProfileEntity tuteeProfile = e.toEntity(member);
                List<SubjectEntity> subjectEntities = saveTuteeSubjects(e.getSubjects(),
                    tuteeProfile);
                tuteeProfile.addSubject(subjectEntities);
                return tuteeProfile;
            }).toList();

            member.saveTuteeProfiles(tuteeProfileList, requestDto.getRole());
            tuteeProfileRepository.saveAll(tuteeProfileList);
        }
        return member;
    }

    private List<SubjectEntity> saveTutorSubjects(List<Subject> subjects,
        TutorProfileEntity tutorProfile) {
        return subjectRepository.saveAll(
            subjects.stream().map(e -> SubjectEntity.of(e, tutorProfile)).toList());
    }

    private List<SubjectEntity> saveTuteeSubjects(List<Subject> subjects,
        TuteeProfileEntity tuteeProfile) {
        return subjectRepository.saveAll(
            subjects.stream().map(e -> SubjectEntity.of(e, tuteeProfile)).toList());
    }
}
