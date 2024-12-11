package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
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
        TutorFilterRequestDto.TutorEnumFilter filterRequest, Pageable pageable) {

        String sql = """
                WITH filtered_profiles AS (
                    SELECT
                        tpe.id AS tutor_id,
                        tpe.imageUrl AS image_url,
                        m.nickname AS tutor_nickname,
                        tpe.university AS tutor_university,
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
                        AND (? IS NULL OR tpe.university = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                        AND (? IS NULL OR tpe.status = ANY(COALESCE(?::text[], ARRAY[]::text[])))
                    GROUP BY
                        tpe.id, m.nickname, tpe.imageUrl, tpe.university, tpe.location
                )
                SELECT *
                FROM filtered_profiles
                ORDER BY tutor_id
                OFFSET ? ROWS FETCH FIRST ? ROWS ONLY
            """;

        String gender = filterRequest.getGender() != null ? filterRequest.getGender().name() : null;
        List<String> subjects = filterRequest.getSubjects() != null
            ? filterRequest.getSubjects().stream().map(Enum::name).toList()
            : null;
        List<String> locations = filterRequest.getLocations() != null
            ? filterRequest.getLocations().stream().map(Location::name).toList()
            : null;
        List<String> universities = filterRequest.getUniversities() != null
            ? filterRequest.getUniversities().stream().map(University::name).toList()
            : null;
        List<String> statusList = filterRequest.getStatusList() != null
            ? filterRequest.getStatusList().stream().map(EnrollmentStatus::name).toList()
            : null;

        log.debug("Gender: {}", gender);
        log.debug("Subjects: {}", subjects);
        log.debug("Locations: {}", locations);
        log.debug("Universities: {}", universities);
        log.debug("StatusList: {}", statusList);

        RowMapper<TutorSimpleResponseDto> rowMapper = (rs, rowNum) -> {

            List<Subject> subjectList = Optional.ofNullable(rs.getArray("subjects"))
                .map(array -> {
                    try {
                        String[] subjectStrings = (String[]) array.getArray();
                        return Arrays.stream(subjectStrings)
                            .map(Subject::valueOf) // String을 Subject enum으로 변환
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
                Optional.ofNullable(rs.getString("tutor_university"))
                    .map(University::valueOf)
                    .orElse(null),
                Optional.ofNullable(rs.getString("tutor_location"))
                    .map(Location::valueOf)
                    .orElse(null),
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

        String countSql = """
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
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND tpe.university = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                        AND (? IS NULL OR (ARRAY_LENGTH(COALESCE(?::text[], ARRAY[]::text[]), 1) > 0 AND tpe.status = ANY(COALESCE(?::text[], ARRAY[]::text[]))))
                    GROUP BY tpe.id
                ) AS count_query
            """;

        Long total = getTotalCount(countSql, gender, subjects, locations, universities, statusList);

        return new PageImpl<>(tutorList, pageable, total);
    }

    private Long getTotalCount(String countSql, String gender, List<String> subjects,
        List<String> locations,
        List<String> universities, List<String> statusList) {
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

    private void setParameter(PreparedStatement ps, int index, String value, Connection connection)
        throws SQLException {
        log.debug("Setting parameter [{}]: {}", index, value != null ? value : "NULL");

        if (value != null) {
            ps.setString(index, value);
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
    }

    private void setArrayParameter(PreparedStatement ps, int index, List<String> values,
        Connection connection)
        throws SQLException {
        log.debug("PreparedStatement Parameter [{}]: {}", index, values != null ? values : "NULL");

        if (values != null && !values.isEmpty()) {
            ps.setArray(index, connection.createArrayOf("text", values.toArray()));
        } else {
            ps.setArray(index, connection.createArrayOf("text", null));
        }
    }
}
