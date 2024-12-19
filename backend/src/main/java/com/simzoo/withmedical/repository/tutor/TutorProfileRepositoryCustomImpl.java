package com.simzoo.withmedical.repository.tutor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.jpa.JPAExpressions.select;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto.TutorSearchFilter;
import com.simzoo.withmedical.dto.tutor.QTutorProfileResponseDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TutorProfileRepositoryCustomImpl implements TutorProfileRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countFilteredProfiles(TutorSearchFilter filterRequestDto) {

        QTutorProfileEntity tutorProfile = QTutorProfileEntity.tutorProfileEntity;
        QSubjectEntity subject = QSubjectEntity.subjectEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        BooleanExpression eqFilterCondition = buildPredicate(filterRequestDto, tutorProfile,
            subject,
            member);

        return select(tutorProfile.count())
            .from(tutorProfile)
            .leftJoin(tutorProfile.member, member)
            .leftJoin(tutorProfile.subjects, subject)
            .where(eqFilterCondition)
            .fetchOne();
    }

    @Override
    public Optional<TutorProfileResponseDto> findTutorProfileDtoById(Long tutorId) {

        QTutorProfileEntity tutorProfile = QTutorProfileEntity.tutorProfileEntity;
        QMemberEntity member = QMemberEntity.memberEntity;
        QSubjectEntity subject = QSubjectEntity.subjectEntity;

        Map<Long, TutorProfileResponseDto> result = jpaQueryFactory
            .from(tutorProfile)
            .leftJoin(member).on(member.id.eq(tutorProfile.member.id))
            .leftJoin(subject).on(subject.tutorProfile.id.eq(tutorProfile.id))
            .where(tutorProfile.id.eq(tutorId))
            .transform(groupBy(tutorProfile.id).as(
                new QTutorProfileResponseDto(
                    tutorProfile.id,
                    tutorProfile.imageUrl,
                    list(subject.subject),
                    tutorProfile.location,
                    tutorProfile.univName,
                    tutorProfile.status,
                    tutorProfile.description
                )
            ));

        TutorProfileResponseDto dtoResult = result.get(tutorId);
        return Optional.ofNullable(dtoResult);
    }

    private BooleanExpression buildPredicate(TutorSearchFilter filterRequest,
        QTutorProfileEntity tutorProfile, QSubjectEntity subject, QMemberEntity member) {
        BooleanExpression predicate = null;

        if (filterRequest.getGender() != null) {
            predicate = combine(predicate, member.gender.eq(filterRequest.getGender()));
        }
        if (filterRequest.getSubjects() != null && !filterRequest.getSubjects().isEmpty()) {
            System.out.println("filterRequest.getSubjects() = " + filterRequest.getSubjects());
            predicate = combine(predicate, subject.subject.in(filterRequest.getSubjects()));
        }
        if (filterRequest.getLocations() != null && !filterRequest.getLocations().isEmpty()) {
            List<java.lang.String> locationNames = filterRequest.getLocations();
            predicate = combine(predicate, tutorProfile.location.stringValue().in(locationNames));
        }
        if (filterRequest.getUniversities() != null && !filterRequest.getUniversities().isEmpty()) {
            List<java.lang.String> stringNames = filterRequest.getUniversities();
            predicate = combine(predicate,
                tutorProfile.univName.in(stringNames));
        }
        if (filterRequest.getStatusList() != null && !filterRequest.getStatusList().isEmpty()) {
            List<java.lang.String> statusNames = filterRequest.getStatusList().stream()
                .map(EnrollmentStatus::name).toList();
            predicate = combine(predicate, tutorProfile.status.stringValue().in(statusNames));
        }

        return predicate;
    }

    private BooleanExpression combine(BooleanExpression base, BooleanExpression addition) {
        return base == null ? addition : base.and(addition);
    }
}
