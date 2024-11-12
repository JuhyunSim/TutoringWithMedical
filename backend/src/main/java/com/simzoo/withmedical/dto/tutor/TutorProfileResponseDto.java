package com.simzoo.withmedical.dto.tutor;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class TutorProfileResponseDto {

    private Long tutorId;
    private String nickname;
    private String imageUrl;
    private Gender gender;
    private List<Subject> subjects = new ArrayList<>();
    private Location location;
    private University university;
    private EnrollmentStatus status;
    private String description;

    @QueryProjection
    public TutorProfileResponseDto(Long tutorId, String nickname, String imageUrl, Gender gender,
        List<Subject> subjects, Location location, University university, EnrollmentStatus status,
        String description) {
        this.tutorId = tutorId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.subjects = subjects;
        this.location = location;
        this.university = university;
        this.status = status;
        this.description = description;
    }
}
