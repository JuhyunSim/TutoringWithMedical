package com.simzoo.withmedical.repository.tutor;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.QTutorSimpleResponseDto;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
import java.util.List;
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

        List<TutorSimpleResponseDto> results = jpaQueryFactory.select(new QTutorSimpleResponseDto(
                tutorProfile.member.nickname,
                tutorProfile.university,
                tutorProfile.location,
                tutorProfile.subjects
            )).from(tutorProfile)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory.select(tutorProfile.count())
            .from(tutorProfile)
            .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
