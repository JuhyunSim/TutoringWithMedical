package com.simzoo.withmedical.dto.tutor;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorProfileResponseDto {
    private Long tutorId;
    private String nickname;
    private Gender gender;
    private List<Subject> subjects = new ArrayList<>();
    private Location location;
    private University university;
    private EnrollmentStatus status;
    private String description;
}
