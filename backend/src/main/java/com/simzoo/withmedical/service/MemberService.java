package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTutorProfileRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.TuteeProfileRepository;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.repository.subject.SubjectRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final TuteeProfileRepository tuteeProfileRepository;

    @Transactional(readOnly = true)
    public void checkMemberExist(String phoneNumber) {
        memberRepository.findByHashedPhoneNumber(phoneNumber).ifPresent(memberEntity -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        });
    }

    @Transactional(readOnly = true)
    public MemberEntity getMyInfo(Long userId) {
        return memberRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public MemberEntity updateMember(Long userId, UpdateMemberRequestDto requestDto) {
        MemberEntity memberEntity = memberRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberEntity.updateInfo(requestDto);
        log.debug("tutor profile = {}", memberEntity.getTutorProfile());
        log.debug("request profile = {}", requestDto.getTutorProfile());

        if (memberEntity.getTutorProfile() != null && requestDto.getTutorProfile() != null) {
            updateTutorSubjectAndProfile(memberEntity, requestDto.getTutorProfile());
        }

        if (memberEntity.getTuteeProfiles() != null && requestDto.getTuteeProfile() != null) {
            updateTuteeSubjectAndProfile(memberEntity, requestDto.getTuteeProfile());
        }

        return memberEntity;
    }

    @Transactional
    public MemberEntity addTuteeProfile(Long userId, Role role, TuteeProfileRequestDto requestDto) {
        MemberEntity memberEntity = memberRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        TuteeProfileEntity tuteeProfile = tuteeProfileRepository.save(
            requestDto.toEntity(memberEntity));

        List<SubjectEntity> subjectEntities = subjectRepository.saveAll(
            requestDto.getSubjects().stream().map(e -> SubjectEntity.of(e, tuteeProfile)).toList());

        tuteeProfile.addSubject(subjectEntities);

        memberEntity.addTuteeProfile(tuteeProfile, role);

        return memberEntity;
    }

    @Transactional
    public MemberEntity updateParentProfile(Long userId, UpdateMemberRequestDto requestDto) {
        MemberEntity memberEntity = memberRepository.findParentWithATuteeProfile(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        memberEntity.updateInfo(requestDto);

        updateTuteeSubjectAndProfile(memberEntity, requestDto.getTuteeProfile());
        return memberEntity;
    }

    @Transactional
    public void removeProfile(Long userId, Long tuteeId) {

        subjectRepository.deleteAllByTuteeProfile_Id(tuteeId);

        TuteeProfileEntity tuteeProfile = tuteeProfileRepository.findByIdAndMember_Id(tuteeId,
                userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROFILE));

        tuteeProfileRepository.delete(tuteeProfile);

        MemberEntity memberEntity = memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        memberEntity.removeTuteeProfile(tuteeProfile);
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

        Map<Long, TuteeProfileEntity> tuteeProfileMap = member.getTuteeProfiles().stream().collect(
            Collectors.toMap(TuteeProfileEntity::getId, e -> e));

        TuteeProfileEntity tuteeProfile = tuteeProfileMap.get(requestDto.getTuteeId());
        //subject 저장
        List<SubjectEntity> subjectEntities = subjectRepository.fetchTuteeSubjects(
                requestDto.getTuteeId());

        List<SubjectEntity> toDelete = subjectEntities.stream()
            .filter(savedSubject -> requestDto.getSubjects().stream()
                .noneMatch(requestSubject -> requestSubject.equals(savedSubject.getSubject())))
            .toList();
        log.debug("toDelete = {}",
            toDelete.isEmpty() ? "No subjects to delete" : toDelete.get(0).getSubject());
        List<SubjectEntity> toAdd = requestDto.getSubjects().stream()
            .filter(requestSubject -> subjectEntities.stream()
                .noneMatch(savedSubject -> savedSubject.getSubject().equals(requestSubject)))
            .map(e -> SubjectEntity.of(e, tuteeProfile))
            .toList();
        log.debug("toAdd = {}",
            toAdd.isEmpty() ? "No subjects to add" : toAdd.get(0).getSubject());
        subjectRepository.deleteAll(toDelete);
        subjectRepository.saveAll(toAdd);
        //tuteeprofile 업데이트
        tuteeProfile.addSubject(subjectRepository.fetchTuteeSubjects(requestDto.getTuteeId()));
        tuteeProfile.updateProfile(requestDto);
    }
}
