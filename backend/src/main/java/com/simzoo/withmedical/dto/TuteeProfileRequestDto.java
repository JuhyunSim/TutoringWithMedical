package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteeProfileEntity;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class TuteeProfileRequestDto {

    private Location location;
    private List<Subject> subjects;
    private String description;
    private TuteeGrade tuteeGrade;

    public TuteeProfileEntity toEntity(MemberEntity member) {
        return TuteeProfileEntity.builder()
            .location(location)
            .description(description)
            .grade(tuteeGrade)
            .subjects(new ArrayList<>())
            .build();
    }
}

