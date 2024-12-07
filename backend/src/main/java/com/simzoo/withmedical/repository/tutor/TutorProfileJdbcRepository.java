package com.simzoo.withmedical.repository.tutor;

import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import com.simzoo.withmedical.dto.filter.TutorFilterRequestDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
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

    public Page<TutorSimpleResponseDto> findFilteredProfiles(TutorFilterRequestDto filterRequest, Pageable pageable) {

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
                    (? IS NULL OR m.gender = ?)
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND s.subject = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.location = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.university = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.status = ANY(?::text[])
                        )
                    )
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

        RowMapper<TutorSimpleResponseDto> rowMapper = (rs, rowNum) -> new TutorSimpleResponseDto(
            rs.getLong("tutor_id"),
            rs.getString("image_url"),
            rs.getString("tutor_nickname"),
            University.valueOf(rs.getString("tutor_university")),
            Location.valueOf(rs.getString("tutor_location")),
            Arrays.asList((Subject[]) rs.getArray("subjects").getArray())
        );

        List<TutorSimpleResponseDto> tutorList = jdbcTemplate.query(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                setParameter(ps, 1, gender, connection);
                setParameter(ps, 2, gender, connection);

                setArrayParameter(ps, 3, subjects, connection);
                setParameter(ps, 4, "{}", connection);
                setArrayParameter(ps, 5, subjects, connection);

                setArrayParameter(ps, 6, locations, connection);
                setParameter(ps, 7, "{}", connection);
                setArrayParameter(ps, 8, locations, connection);

                setArrayParameter(ps, 9, universities, connection);
                setParameter(ps, 10, "{}", connection);
                setArrayParameter(ps, 11, universities, connection);

                setArrayParameter(ps, 12, statusList, connection);
                setParameter(ps, 13, "{}", connection);
                setArrayParameter(ps, 14, statusList, connection);

                ps.setLong(15, pageable.getOffset());
                ps.setInt(16, pageable.getPageSize());

                return ps;
            },
            rowMapper
        );

        String countSql = """
            SELECT COUNT(*)
            FROM (
                SELECT tpe.id
                FROM tutorProfile tpe
                LEFT JOIN member m ON tpe.memberId = m.id
                LEFT JOIN SubjectEntity s ON tpe.id = s.tutorId
                WHERE
                    (? IS NULL OR m.gender = ?)
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND s.subject = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.location = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.university = ANY(?::text[])
                        )
                    )
                    AND (
                        ? IS NULL OR (
                            ? != '{}' AND tpe.status = ANY(?::text[])
                        )
                    )
                GROUP BY tpe.id
            ) AS count_query
        """;

        Long total = getTotalCount(countSql, gender, subjects, locations, universities, statusList);

        return new PageImpl<>(tutorList, pageable, total);
    }

    private Long getTotalCount(String countSql, String gender, List<String> subjects, List<String> locations,
        List<String> universities, List<String> statusList) {
        return jdbcTemplate.queryForObject(
            countSql,
            (rs, rowNum) -> rs.getLong(1),
            gender,
            createSqlArray("text", subjects),
            createSqlArray("text", locations),
            createSqlArray("text", universities),
            createSqlArray("text", statusList)
        );
    }

    private Array createSqlArray(String typeName, List<String> values) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            return connection.createArrayOf(typeName, values != null ? values.toArray() : new String[]{});
        } catch (SQLException e) {
            throw new RuntimeException("Error creating SQL Array", e);
        }
    }

    private void setParameter(PreparedStatement ps, int index, String value, Connection connection)
        throws SQLException {
        if (value != null) {
            ps.setString(index, value);
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
    }

    private void setArrayParameter(PreparedStatement ps, int index, List<String> values, Connection connection)
        throws SQLException {
        log.debug("PreparedStatement Parameter [{}]: {}", index, values);

        if (values != null && !values.isEmpty()) {
            ps.setArray(index, connection.createArrayOf("text", values.toArray()));
        } else {
            ps.setNull(index, Types.ARRAY);
        }
    }
}
