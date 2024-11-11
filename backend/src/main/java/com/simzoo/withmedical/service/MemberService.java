package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.UpdateMemberRequestDto;
import com.simzoo.withmedical.dto.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.UpdateMemberRequestDto.UpdateTutorProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public void checkMemberExist(String phoneNumber) {
        memberRepository.findByHashedPhoneNumber(phoneNumber).ifPresent(memberEntity -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        });
    }

    @Transactional(readOnly = true)
    public MemberEntity getMyInfo(Long userId) {
        return memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public MemberEntity updateMember(Long userId, UpdateMemberRequestDto requestDto) {
        MemberEntity memberEntity = memberRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberEntity.updateInfo(requestDto);
        log.info("tutor profile = {}", memberEntity.getTutorProfile());
        log.info("request profile = {}", requestDto.getTutorProfile());

        if (memberEntity.getTutorProfile() != null && requestDto.getTutorProfile() != null) {
            updateTutorSubjectAndProfile(memberEntity, requestDto.getTutorProfile());
        }

        if (memberEntity.getTuteeProfile() != null && requestDto.getTuteeProfile() != null) {
            updateTuteeSubjectAndProfile(memberEntity, requestDto.getTuteeProfile());
        }

        return memberEntity;
    }

    private void updateTutorSubjectAndProfile(MemberEntity member,
        UpdateTutorProfileRequestDto requestDto) {

        TutorProfileEntity tutorProfileEntity = member.getTutorProfile();
        //subject 저장
        List<SubjectEntity> subjectEntities = subjectRepository.fetchTutorSubjects(
            tutorProfileEntity.getId());

        List<Subject> newSubjects = requestDto.getSubjects();
        List<SubjectEntity> toDelete = subjectEntities.stream()
            .filter(e -> !newSubjects.contains(e.getSubject())).toList();
        List<SubjectEntity> toAdd = newSubjects.stream().filter(
                e -> !subjectEntities.stream().map(SubjectEntity::getSubject).toList().contains(e))
            .map(e -> SubjectEntity.of(e, tutorProfileEntity)).toList();

        subjectRepository.deleteAll(toDelete);
        subjectRepository.saveAll(toAdd);

        //tutorProfile 저장
        tutorProfileEntity.updateSubjects(
            subjectRepository.fetchTutorSubjects(tutorProfileEntity.getId()));
        tutorProfileEntity.updateProfile(requestDto);
        log.info("tutor subjects: {}",
            tutorProfileEntity.getSubjects().stream().map(SubjectEntity::getSubject).toList());
    }

    private void updateTuteeSubjectAndProfile(MemberEntity member,
        UpdateTuteeProfileRequestDto requestDto) {
        TuteeProfileEntity tuteeProfile = member.getTuteeProfile();
        //subject 저장
        List<SubjectEntity> subjectEntities = subjectRepository.fetchTutorSubjects(
            tuteeProfile.getId());
        subjectEntities = requestDto.getSubjects().stream()
            .map(e -> SubjectEntity.of(e, tuteeProfile)).toList();
        //tutorProfile 저장
        tuteeProfile.updateProfile(requestDto);
    }
}
