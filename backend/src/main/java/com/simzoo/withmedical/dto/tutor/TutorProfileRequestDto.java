package com.simzoo.withmedical.dto.tutor;

import com.simzoo.withmedical.dto.location.LocationDto;
import com.simzoo.withmedical.dto.school.SchoolDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Subject;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorProfileRequestDto {
    private String imageUrl;
    private List<Subject> subjects;
    private LocationDto location;
    private String description;
    private SchoolDto university;
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
