package com.simzoo.withmedical.dto.filter;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorFilterRequestDto {
    Gender gender;
    List<Subject> subjects;
    List<Location> locations;
    List<University> universities;
    List<EnrollmentStatus> statusList;
}
