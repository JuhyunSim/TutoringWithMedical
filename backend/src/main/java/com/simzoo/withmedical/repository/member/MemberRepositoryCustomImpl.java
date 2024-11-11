package com.simzoo.withmedical.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.QMemberEntity;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.QTuteeProfileEntity;
import com.simzoo.withmedical.entity.QTutorProfileEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MemberEntity> findByIdWithProfile(Long memberId) {

        QMemberEntity member = QMemberEntity.memberEntity;
        QTutorProfileEntity tutorProfile = QTutorProfileEntity.tutorProfileEntity;
        QTuteeProfileEntity tuteeProfile = QTuteeProfileEntity.tuteeProfileEntity;
        QSubjectEntity subject = QSubjectEntity.subjectEntity;

        return Optional.ofNullable(queryFactory.selectFrom(member)
            .leftJoin(member.tutorProfile, tutorProfile).fetchJoin()
            .leftJoin(member.tuteeProfile, tuteeProfile).fetchJoin()
            .where(member.id.eq(memberId))
            .fetchOne());
    }
}
