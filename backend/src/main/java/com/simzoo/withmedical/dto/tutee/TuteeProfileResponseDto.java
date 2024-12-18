package com.simzoo.withmedical.dto.tutee;

import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TuteeProfileResponseDto {

    private Long tuteeId;
    private String tuteeName;
    private Gender gender;
    private List<Subject> subjects = new ArrayList<>();
    private String location;
    private TuteeGrade tuteeGrade;
    private String personality;
    private String description;

}
