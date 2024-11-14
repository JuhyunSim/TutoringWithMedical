package com.simzoo.withmedical.dto.tutee;

import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ParentTuteeProfileResponseDto {

    private Long tuteeId;
    private String tuteeName;
    private Gender gender;
    private List<Subject> subjects = new ArrayList<>();
    private Location location;
    private TuteeGrade tuteeGrade;
    private String description;

}
