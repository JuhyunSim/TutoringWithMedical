package com.simzoo.withmedical.repository.tutor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.QTutorSimpleResponseDto;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.dto.tutor.QTutorProfileResponseDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.University;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TutorProfileRepositoryCustomImpl implements TutorProfileRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TutorSimpleResponseDto> findTutorProfileDtos(Pageable pageable,
        TutorFilterRequestDto filterRequest) {

        QTutorProfileEntity tutorProfile = QTutorProfileEntity.tutorProfileEntity;
        QMemberEntity member = QMemberEntity.memberEntity;
        QSubjectEntity subject = QSubjectEntity.subjectEntity;

        BooleanExpression eqFilterCondition = buildPredicate(filterRequest, tutorProfile, subject, member);
        List<TutorSimpleResponseDto> tutorList = jpaQueryFactory
            .selectDistinct(new QTutorSimpleResponseDto(
                tutorProfile.id,
                tutorProfile.imageUrl,
                member.nickname,
                tutorProfile.university,
                tutorProfile.location,
                list(subject.subject)
            ))
            .from(tutorProfile)
            .leftJoin(tutorProfile.member, member)
            .leftJoin(tutorProfile.subjects, subject)
            .where(eqFilterCondition)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(tutorProfile.count())
            .from(tutorProfile)
            .leftJoin(tutorProfile.member, member)
            .leftJoin(tutorProfile.subjects, subject)
            .where(eqFilterCondition)
            .fetchOne();

        return new PageImpl<>(tutorList, pageable, total);
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
                    tutorProfile.university,
                    tutorProfile.status,
                    tutorProfile.description
                )
            ));

        TutorProfileResponseDto dtoResult = result.get(tutorId);
        return Optional.ofNullable(dtoResult);
    }

    private BooleanExpression buildPredicate(TutorFilterRequestDto filterRequest,
        QTutorProfileEntity tutorProfile, QSubjectEntity subject, QMemberEntity member) {
        BooleanExpression predicate = null;

        if (filterRequest.getGender() != null) {
            predicate = combine(predicate, member.gender.eq(filterRequest.getGender()));
        }
        if (filterRequest.getSubjects() != null && !filterRequest.getSubjects().isEmpty()) {
            predicate = combine(predicate, subject.subject.in(filterRequest.getSubjects()));
        }
        if (filterRequest.getLocations() != null && !filterRequest.getLocations().isEmpty()) {
            List<String> locationNames = filterRequest.getLocations().stream().map(Location::name).toList();
            predicate = combine(predicate, tutorProfile.location.stringValue().in(locationNames));
        }
        if (filterRequest.getUniversities() != null && !filterRequest.getUniversities().isEmpty()) {
            List<String> universityNames = filterRequest.getUniversities().stream().map(University::name).toList();
            predicate = combine(predicate, tutorProfile.university.stringValue().in(universityNames));
        }
        if (filterRequest.getStatusList() != null && !filterRequest.getStatusList().isEmpty()) {
            List<String> statusNames = filterRequest.getStatusList().stream().map(EnrollmentStatus::name).toList();
            predicate = combine(predicate, tutorProfile.status.stringValue().in(statusNames));
        }

        return predicate;
    }

    private BooleanExpression combine(BooleanExpression base, BooleanExpression addition) {
        return base == null ? addition : base.and(addition);
    }
}
