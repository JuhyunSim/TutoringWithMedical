package com.simzoo.withmedical.dto.tutee;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ParentTuteeProfileRequestDto {

    @NotBlank
    private String tuteeName;

    @NotNull
    private Gender gender;

    @NotNull
    private Location location;
    @NotNull
    private List<Subject> subjects;
    private String description;
    @NotNull
    private TuteeGrade tuteeGrade;

    public TuteeProfileEntity toEntity(MemberEntity member) {
        return TuteeProfileEntity.builder()
            .member(member)
            .name(tuteeName)
            .gender(gender)
            .location(location)
            .description(description)
            .grade(tuteeGrade)
            .subjects(new ArrayList<>())
            .build();
    }

}
