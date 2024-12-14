package com.simzoo.withmedical.repository.tutor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.jpa.JPAExpressions.select;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import com.simzoo.withmedical.enums.Subject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Arrays;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countFilteredProfiles(TutorFilterRequestDto.TutorEnumFilter filterRequestDto) {

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
    public Page<TutorSimpleResponseDto> findFilteredProfiles(
        TutorFilterRequestDto.TutorEnumFilter filterRequest,
        Pageable pageable) {
        java.lang.String sql = """
                WITH filtered_profiles AS (
                    SELECT
                        tpe.id AS tutor_id,
                        tpe.imageUrl AS image_url,
                        m.nickname AS tutor_nickname,
                        tpe.string AS tutor_university,
                        tpe.location AS tutor_location,
                        ARRAY_AGG(s.subject) AS subjects
                    FROM
                        tutorProfile tpe
                    LEFT JOIN
                        member m ON tpe.memberId = m.id
                    LEFT JOIN
                        SubjectEntity s ON tpe.id = s.tutorId
                    WHERE
                        (:gender IS NULL OR m.gender = :gender)
                        AND (:subjects IS NULL OR s.subject = ANY(:subjects))
                        AND (:locations IS NULL OR tpe.location = ANY(:locations))
                        AND (:universities IS NULL OR tpe.string = ANY(:universities))
                        AND (:statuslist IS NULL OR tpe.status = ANY(:statuslist))
                    GROUP BY
                        tpe.id, m.nickname, tpe.imageUrl, tpe.string, tpe.location
                )
                SELECT *
                FROM filtered_profiles
                ORDER BY tutor_id
                OFFSET :offset ROWS FETCH FIRST :limit ROWS ONLY
            """;

        // 필터링 값 추출
        java.lang.String gender = filterRequest.getGender() != null ? filterRequest.getGender().name() : null;
        java.lang.String[] subjects = filterRequest.getSubjects() != null
            ? filterRequest.getSubjects().stream().map(Enum::name).toArray(java.lang.String[]::new)
            : new java.lang.String[0]; // null 대신 빈 배열로 처리
        java.lang.String[] locations = filterRequest.getLocations() != null
            ? filterRequest.getLocations().stream().map(Location::name).toArray(java.lang.String[]::new)
            : new java.lang.String[0];
        java.lang.String[] universities = filterRequest.getUniversities() != null
            ? filterRequest.getUniversities().stream().map(University::name).toArray(java.lang.String[]::new)
            : new java.lang.String[0];
        java.lang.String[] statusList = filterRequest.getStatusList() != null
            ? filterRequest.getStatusList().stream().map(EnrollmentStatus::name)
            .toArray(java.lang.String[]::new)
            : new java.lang.String[0];

        // 페이징
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();

        // 페이징 처리된 데이터 쿼리
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("gender", gender);
        query.setParameter("subjects", subjects);
        query.setParameter("locations", locations); // NPE 방지
        query.setParameter("universities", universities);
        query.setParameter("statuslist", statusList);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        // 전체 데이터 개수 쿼리
        long total = ((Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM ...") // Simplified for brevity
            .setParameter("gender", gender)
            .setParameter("subjects", subjects)
            .setParameter("locations", locations) // NPE 방지
            .setParameter("universities", universities)
            .setParameter("statuslist", statusList)
            .getSingleResult()).longValue();

        // 결과를 DTO로 변환
        List<TutorSimpleResponseDto> tutorList = results.stream()
            .map(row -> new TutorSimpleResponseDto(
                (Long) row[0], // tutor_id
                (java.lang.String) row[1], // image_url
                (java.lang.String) row[2], // tutor_nickname
                (java.lang.String) row[3], // tutor_university
                (java.lang.String) row[4], // tutor_location
                Arrays.asList((Subject[]) row[5]) // subjects as List<String>
            ))
            .toList();

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
                    tutorProfile.univName,
                    tutorProfile.status,
                    tutorProfile.description
                )
            ));

        TutorProfileResponseDto dtoResult = result.get(tutorId);
        return Optional.ofNullable(dtoResult);
    }

    private BooleanExpression buildPredicate(TutorFilterRequestDto.TutorEnumFilter filterRequest,
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
            List<java.lang.String> locationNames = filterRequest.getLocations().stream().map(Location::name)
                .toList();
            predicate = combine(predicate, tutorProfile.location.stringValue().in(locationNames));
        }
        if (filterRequest.getUniversities() != null && !filterRequest.getUniversities().isEmpty()) {
            List<java.lang.String> stringNames = filterRequest.getUniversities().stream()
                .map(University::name).toList();
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
