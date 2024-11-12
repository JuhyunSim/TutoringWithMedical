package com.simzoo.withmedical.dto.tutor;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorProfileRequestDto {
    private String proofFileUrl;
    private String imageUrl;
    private List<Subject> subjects;
    private Location location;
    private String description;
    private University university;
    private EnrollmentStatus status;

    public TutorProfileEntity toEntity(MemberEntity member) {
        return TutorProfileEntity.builder()
            .memberId(member.getId())
            .imageUrl(imageUrl)
            .location(location)
            .description(description)
            .university(university)
            .status(status)
            .subjects(new ArrayList<>())
            .build();
    }
}
