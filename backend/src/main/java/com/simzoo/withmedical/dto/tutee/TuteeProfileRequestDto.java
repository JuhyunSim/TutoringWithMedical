package com.simzoo.withmedical.dto.tutee;

import com.simzoo.withmedical.dto.LocationDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TuteeProfileRequestDto {

    private String tuteeName;

    private Gender gender;
    @NotNull
    private LocationDto location;
    @NotNull
    private List<Subject> subjects;

    private String personality;
    private String description;
    @NotNull
    private TuteeGrade tuteeGrade;

    public TuteeProfileEntity toEntity(MemberEntity member) {
        return TuteeProfileEntity.builder()
            .member(member)
            .name(tuteeName)
            .gender(gender)
            .location(location.getSigungu().getFull_addr())
            .personality(personality)
            .description(description)
            .grade(tuteeGrade)
            .subjects(new ArrayList<>())
            .build();
    }
}

