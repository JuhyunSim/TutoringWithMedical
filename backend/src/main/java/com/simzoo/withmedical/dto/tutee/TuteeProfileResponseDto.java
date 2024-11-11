package com.simzoo.withmedical.dto.tutee;

import com.simzoo.withmedical.enums.Location;
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
    private List<Subject> subjects = new ArrayList<>();
    private Location location;
    private TuteeGrade tuteeGrade;
    private String description;

}
