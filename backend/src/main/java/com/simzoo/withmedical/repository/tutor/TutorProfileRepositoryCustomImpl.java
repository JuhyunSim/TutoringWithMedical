package com.simzoo.withmedical.repository.tutor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.QTutorSimpleResponseDto;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.tutor.QTutorProfileResponseDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
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
    public Page<TutorSimpleResponseDto> findTutorProfileDtos(Pageable pageable) {

        QTutorProfileEntity tutorProfile = QTutorProfileEntity.tutorProfileEntity;
        QMemberEntity member = QMemberEntity.memberEntity;
        QSubjectEntity subject = QSubjectEntity.subjectEntity;

        Map<Long, TutorSimpleResponseDto> results = jpaQueryFactory
            .from(tutorProfile)
            .leftJoin(member).on(member.id.eq(tutorProfile.memberId))
            .leftJoin(subject).on(subject.tutorProfile.id.eq(tutorProfile.id))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(groupBy(tutorProfile.id).as(
                new QTutorSimpleResponseDto(
                    tutorProfile.id,
                    tutorProfile.imageUrl,
                    member.nickname,
                    tutorProfile.university,
                    tutorProfile.location,
                    list(subject.subject)
                )
            ));

        List<TutorSimpleResponseDto> tutorList = List.copyOf(results.values());

        Long total = jpaQueryFactory
            .select(tutorProfile.count())
            .from(tutorProfile)
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
            .leftJoin(member).on(member.id.eq(tutorProfile.memberId))
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
}
