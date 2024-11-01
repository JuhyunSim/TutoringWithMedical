package com.simzoo.withmedical.dto.tutor;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TutorProfileEntity;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class TutorProfileRequestDto {
    private MultipartFile proofFile;
    private MultipartFile profileImage;
    private List<Subject> subjects;
    private Location location;
    private String description;
    private University university;
    private EnrollmentStatus status;

    public TutorProfileEntity toEntity(MemberEntity member) {
        return TutorProfileEntity.builder()
            .subjects(subjects)
            .location(location)
            .description(description)
            .university(university)
            .status(status)
            .member(member)
            .build();
    }
}
