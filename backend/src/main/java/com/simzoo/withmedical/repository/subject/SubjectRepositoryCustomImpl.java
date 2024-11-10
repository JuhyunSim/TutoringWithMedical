package com.simzoo.withmedical.repository.subject;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.entity.QSubjectEntity;
import com.simzoo.withmedical.entity.SubjectEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectRepositoryCustomImpl implements SubjectRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubjectEntity> fetchTutorSubjects(Long tutorProfileId) {
        QSubjectEntity subjectEntity = QSubjectEntity.subjectEntity;

        return queryFactory.selectFrom(subjectEntity)
            .where(subjectEntity.tutorId.eq(tutorProfileId))
            .fetch();
    }

    @Override
    public List<SubjectEntity> fetchTuteeSubjects(Long tuteeProfileId) {
        QSubjectEntity subjectEntity = QSubjectEntity.subjectEntity;

        return queryFactory.selectFrom(subjectEntity)
            .where(subjectEntity.tutorId.eq(tuteeProfileId))
            .fetch();
    }

}
