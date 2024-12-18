package com.simzoo.withmedical.dto.tutor;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Subject;
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
    private String imageUrl;
    private List<Subject> subjects = new ArrayList<>();
    private String location;
    private String univName;
    private EnrollmentStatus status;
    private String description;

    @QueryProjection
    public TutorProfileResponseDto(Long tutorId, String imageUrl, List<Subject> subjects,
        String location, String univName, EnrollmentStatus status,
        String description) {
        this.tutorId = tutorId;
        this.imageUrl = imageUrl;
        this.subjects = subjects;
        this.location = location;
        this.univName = univName;
        this.status = status;
        this.description = description;
    }
}
