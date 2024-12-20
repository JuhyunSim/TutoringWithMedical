package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto.TutorSearchFilter;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TutorProfileJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Page<TutorSimpleResponseDto> findFilteredProfiles(
        TutorSearchFilter filterRequest, Pageable pageable) {

        java.lang.String sql = """
                WITH filtered_profiles AS (
                    SELECT
                        tpe.id AS tutor_id,
                        tpe.imageUrl AS image_url,
                        m.nickname AS tutor_nickname,
                        tpe.univName AS tutor_university,
                        tpe.location AS tutor_location,
                        ARRAY_AGG(s.subject) AS subjects
                    FROM
                        tutorProfile tpe
                    LEFT JOIN
                        member m ON tpe.memberId = m.id
                    LEFT JOIN
                        SubjectEntity s ON tpe.id = s.tutorId
                    WHERE
                        (? IS NULL OR m.gender = COALESCE(?, m.gender))
                        AND (? IS NULL OR s.subject = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                        AND (? IS NULL OR tpe.location = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                        AND (? IS NULL OR tpe.univName = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                        AND (? IS NULL OR tpe.status = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                    GROUP BY
                        tpe.id, m.nickname, tpe.imageUrl, tpe.univName, tpe.location
                )
                SELECT *
                FROM filtered_profiles
                ORDER BY tutor_id
                OFFSET ? ROWS FETCH FIRST ? ROWS ONLY
            """;

        java.lang.String gender = filterRequest.getGender() != null ? filterRequest.getGender().name() : null;
        List<java.lang.String> subjects = filterRequest.getSubjects() != null
            ? filterRequest.getSubjects().stream().map(Enum::name).toList()
            : null;
        List<java.lang.String> locations = filterRequest.getLocations() != null
            ? filterRequest.getLocations() : null;
        List<java.lang.String> universities = filterRequest.getUniversities() != null
            ? filterRequest.getUniversities() : null;
        List<java.lang.String> statusList = filterRequest.getStatusList() != null
            ? filterRequest.getStatusList().stream().map(EnrollmentStatus::name).toList()
            : null;

        log.debug("Gender: {}", gender);
        log.debug("Subjects: {}", subjects);
        log.debug("Locations: {}", locations);
        log.debug("Universities: {}", universities);
        log.debug("StatusList: {}", statusList);

        RowMapper<TutorSimpleResponseDto> rowMapper = (rs, rowNum) -> {

            List<String> subjectList = Optional.ofNullable(rs.getArray("subjects"))
                .map(array -> {
                    try {
                        java.lang.String[] subjectStrings = (java.lang.String[]) array.getArray();
                        return Arrays.stream(subjectStrings)
                            .map(Subject::valueOf)
                            .map(Subject::getDescription)
                            .toList();
                    } catch (SQLException e) {
                        throw new RuntimeException("Error mapping subjects", e);
                    }
                })
                .orElse(Collections.emptyList());

            return new TutorSimpleResponseDto(
                rs.getLong("tutor_id"),
                rs.getString("image_url"),
                rs.getString("tutor_nickname"),
                rs.getString("tutor_university"),
                rs.getString("tutor_location"),
                subjectList // 변환된 Subject 리스트
            );
        };

        List<TutorSimpleResponseDto> tutorList = jdbcTemplate.query(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                setParameter(ps, 1, gender, connection);
                setParameter(ps, 2, gender, connection);

                setArrayParameter(ps, 3, subjects, connection);
                setArrayParameter(ps, 4, subjects, connection);

                setArrayParameter(ps, 5, locations, connection);
                setArrayParameter(ps, 6, locations, connection);

                setArrayParameter(ps, 7, universities, connection);
                setArrayParameter(ps, 8, universities, connection);

                setArrayParameter(ps, 9, statusList, connection);
                setArrayParameter(ps, 10, statusList, connection);

                ps.setLong(11, pageable.getOffset());
                ps.setInt(12, pageable.getPageSize());

                log.debug("PreparedStatement parameters: {}", ps.toString());

                log.debug("offset: {}", pageable.getOffset());
                log.debug("limit: {}", pageable.getPageSize());

                log.debug("Final SQL Query: {}", ps.toString());

                return ps;
            },
            rowMapper
        );

        log.debug("Number of results: {}", tutorList.size());

        java.lang.String countSql = """
                SELECT COUNT(*)
                FROM (
                    SELECT tpe.id
                    FROM tutorProfile tpe
                    LEFT JOIN member m ON tpe.memberId = m.id
                    LEFT JOIN SubjectEntity s ON tpe.id = s.tutorId
                    WHERE
                        (? IS NULL OR m.gender = COALESCE(?, m.gender))
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND s.subject = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND tpe.location = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND tpe.univName = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND tpe.status = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                    GROUP BY tpe.id
                ) AS count_query
            """;

        Long total = getTotalCount(countSql, gender, subjects, locations, universities, statusList);

        return new PageImpl<>(tutorList, pageable, total);
    }

    private Long getTotalCount(
        java.lang.String countSql, java.lang.String gender, List<java.lang.String> subjects,
        List<java.lang.String> locations,
        List<java.lang.String> universities, List<java.lang.String> statusList) {
        return jdbcTemplate.query(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(countSql);
                ps.setString(1, gender); // 1. Gender IS NULL
                ps.setString(2, gender); // 2. Gender 값 설정
                setArrayParameter(ps, 3, subjects, connection);
                setArrayParameter(ps, 4, subjects, connection);
                setArrayParameter(ps, 5, subjects, connection);
                setArrayParameter(ps, 6, locations, connection);
                setArrayParameter(ps, 7, locations, connection);
                setArrayParameter(ps, 8, locations, connection);
                setArrayParameter(ps, 9, universities, connection);
                setArrayParameter(ps, 10, universities, connection);
                setArrayParameter(ps, 11, universities, connection);
                setArrayParameter(ps, 12, statusList, connection);
                setArrayParameter(ps, 13, statusList, connection);
                setArrayParameter(ps, 14, statusList, connection);

                log.debug("Final SQL Query: {}", ps.toString());

                return ps;
            },
            rs -> {
                if (rs.next()) {
                    return rs.getLong(1); // 단일 값 반환
                }
                return 0L; // 결과가 없을 경우 0 반환
            }
        );
    }

    private void setParameter(PreparedStatement ps, int index, java.lang.String value, Connection connection)
        throws SQLException {
        log.debug("Setting parameter [{}]: {}", index, value != null ? value : "NULL");

        if (value != null) {
            ps.setString(index, value);
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
    }

    private void setArrayParameter(PreparedStatement ps, int index, List<java.lang.String> values,
        Connection connection)
        throws SQLException {
        log.debug("PreparedStatement Parameter [{}]: {}", index, values != null ? values : "NULL");

        if (values != null && !values.isEmpty()) {
            ps.setArray(index, connection.createArrayOf("text", values.toArray()));
        } else {
            ps.setArray(index, connection.createArrayOf("text", null));
        }
    }


    public Optional<TutorProfileResponseDto> findTutorProfileDtoById(Long tutorId) {

        String sql = """
                        SELECT 
                            tpe.id AS tutor_id,
                            tpe.imageUrl AS image_url,
                            m.gender As gender,
                            m.nickname AS nickname,
                            tpe.univName AS univ_name,
                            tpe.location AS location,
                            tpe.status AS status,
                            tpe.description AS description,
                            ARRAY_AGG(s.subject) AS subjects
                        FROM 
                            tutorProfile tpe
                        LEFT JOIN 
                            member m ON tpe.memberId = m.id
                        LEFT JOIN 
                            SubjectEntity s ON s.tutorId = tpe.id
                        WHERE 
                            tpe.id = ?
                        GROUP BY 
                            tpe.id, m.nickname, m.gender, tpe.imageUrl, tpe.univName, tpe.location, tpe.status, tpe.description
                    """;

        // RowMapper를 사용해 데이터를 매핑
        RowMapper<TutorProfileResponseDto> rowMapper = (rs, rowNum) -> {
            // subjects 변환 로직
            List<String> subjects = Optional.ofNullable(rs.getArray("subjects"))
                .map(array -> {
                    try {
                        String[] subjectStrings = (String[]) array.getArray();
                            return Arrays.stream(subjectStrings)
                                .map(subjectName -> Subject.valueOf(subjectName).getDescription())
                                .toList();
                    } catch (SQLException e) {
                        throw new RuntimeException("Error mapping subjects", e);
                    }
                })
                .orElse(Collections.emptyList()); // Null-safe 처리

            String status = Optional.ofNullable(rs.getString("status"))
                .map(e -> EnrollmentStatus.valueOf(e).getDescription())
                .orElse("");

            String gender = Optional.ofNullable(rs.getString("gender"))
                .map(e -> Gender.valueOf(e).getDescription())
                .orElse("");

            // DTO 생성 및 반환
            return new TutorProfileResponseDto(
                rs.getLong("tutor_id"),       // tutorId
                rs.getString("nickname"),
                gender,
                rs.getString("image_url"),    // imageUrl
                subjects,                     // List<String> subjects
                rs.getString("location"),     // location
                rs.getString("univ_name"),    // univName
                status,                       // status
                rs.getString("description")   // description
            );
        };

        // Query 실행
        List<TutorProfileResponseDto> results = jdbcTemplate.query(sql, rowMapper, tutorId);

        // 결과 반환
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
