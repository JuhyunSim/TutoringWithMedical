package com.simzoo.withmedical.dto.tutor;

import com.simzoo.withmedical.dto.location.LocationDto;
import com.simzoo.withmedical.dto.school.SchoolDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Subject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorProfileRequestDto {
    private String imageUrl;
    @NotNull
    @NotEmpty
    private List<Subject> subjects;
    @NotNull
    private LocationDto location;
    private String description;
    @NotNull
    private SchoolDto university;
    @NotNull
    private EnrollmentStatus status;

    public TutorProfileEntity toEntity(MemberEntity member) {
        return TutorProfileEntity.builder()
            .member(member)
            .imageUrl(imageUrl)
            .location(location.getSigungu().getFull_addr())
            .description(description)
            .univName(university.getSchoolName())
            .univNumber(university.getSeq())
            .status(status)
            .subjects(new ArrayList<>())
            .build();
    }
}
