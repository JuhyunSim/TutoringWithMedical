package com.simzoo.withmedical.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.QReviewResponseDto;
import com.simzoo.withmedical.dto.ReviewResponseDto;
import com.simzoo.withmedical.entity.QReviewEntity;
import com.simzoo.withmedical.util.OrderSpecifierUtil;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewResponseDto> findReviewsByTutorId(Long tutorId, Pageable pageable) {

        QReviewEntity review = QReviewEntity.reviewEntity;

        Long total = queryFactory.select(review.count())
            .from(review)
            .where(review.tutorProfile.id.eq(tutorId))
            .fetchOne();

        List<ReviewResponseDto> reviews = queryFactory.select(new QReviewResponseDto(
            review.tutorProfile.member.nickname,
            review.writer.nickname,
            review.rating,
            review.content,
            review.createdAt,
            review.updatedAt))
            .from(review)
            .where(review.tutorProfile.id.eq(tutorId))
            .orderBy(OrderSpecifierUtil.getAllOrderSpecifiers(pageable, "reviewEntity"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(reviews, pageable, total);
    }
}
