package com.simzoo.withmedical.repository.tuteePost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.dto.filter.FilterRequestDto;
import com.simzoo.withmedical.dto.tuteePost.QTuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QTuteePostEntity;
import com.simzoo.withmedical.entity.QTuteeProfileEntity;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import com.simzoo.withmedical.util.OrderSpecifierUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TuteePostRepositoryCustomImpl implements TuteePostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TuteePostingSimpleResponseDto> findAllTuteePostings(Pageable pageable,
        List<SortRequestDto<TuteePostSortCriteria>> sortRequests,
        FilterRequestDto filterRequest) {

        QMemberEntity member = QMemberEntity.memberEntity;
        QTuteePostEntity tuteePost = QTuteePostEntity.tuteePostEntity;
        QTuteeProfileEntity tuteeProfile = QTuteeProfileEntity.tuteeProfileEntity;

        // 기본 쿼리 생성
        var query = queryFactory
            .selectFrom(tuteePost)
            .join(tuteePost.tuteeProfile, tuteeProfile)
            .join(tuteePost.tuteeProfile.member, member);

        // 필터 조건 추가
        var predicate = buildPredicate(filterRequest, tuteePost, member);
        if (predicate != null) {
            query.where(predicate);
        }
        // 정렬 조건 추가
        OrderSpecifier<?>[] orderSpecifiers = OrderSpecifierUtil.getAllOrderSpecifiers(sortRequests,
            pageable,
            "tuteePostEntity", "createdAt", Order.DESC);
        query.orderBy(orderSpecifiers);

        // 페이징 처리
        long total = query.fetchCount();

        List<TuteePostingSimpleResponseDto> results = query.select(
                new QTuteePostingSimpleResponseDto(
                    tuteePost.id, tuteePost.tuteeProfile.member.id,
                    tuteePost.tuteeProfile.member.nickname, tuteePost.tuteeProfile.gender,
                    tuteePost.tuteeProfile.grade, tuteePost.tuteeProfile.school,
                    tuteePost.tuteeProfile.personality, tuteePost.type, tuteePost.possibleSchedule,
                    tuteePost.level, tuteePost.fee
                ))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression buildPredicate(FilterRequestDto filterRequest,
        QTuteePostEntity tuteePost, QMemberEntity member) {
        BooleanExpression predicate = null;

        if (filterRequest.getGender() != null) {
            predicate = combine(predicate, member.gender.eq(filterRequest.getGender()));
        }
        if (filterRequest.getTuteeGradeType() != null) {
            predicate = combine(predicate,
                tuteePost.tuteeProfile.gradeGroup.eq(filterRequest.getTuteeGradeType()));
        }
        if (filterRequest.getTutoringType() != null) {
            predicate = combine(predicate, tuteePost.type.eq(filterRequest.getTutoringType()));
        }

        return predicate;
    }

    private BooleanExpression combine(BooleanExpression base, BooleanExpression addition) {
        return base == null ? addition : base.and(addition);
    }
}
