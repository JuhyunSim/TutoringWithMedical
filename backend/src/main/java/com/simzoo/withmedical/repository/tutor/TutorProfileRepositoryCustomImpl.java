package com.simzoo.withmedical.repository.tutor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.QTutorSimpleResponseDto;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
import java.util.List;
import java.util.Map;
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
            .leftJoin(tutorProfile.subjects, subject)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(groupBy(tutorProfile.id).as(
                new QTutorSimpleResponseDto(
                    tutorProfile.id,
                    member.nickname,
                    tutorProfile.university,
                    tutorProfile.location,
                    list(subject.subject) // subjects 필드를 List로 변환
                )
            ));

        List<TutorSimpleResponseDto> tutorList = List.copyOf(results.values());

        Long total = jpaQueryFactory
            .select(tutorProfile.count())
            .from(tutorProfile)
            .fetchOne();

        return new PageImpl<>(tutorList, pageable, total);
    }
}
