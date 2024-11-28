package com.simzoo.withmedical.dto.filter;

import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.GradeType;
import com.simzoo.withmedical.enums.TutoringType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterRequestDto {
    Gender gender;
    GradeType tuteeGradeType;
    TutoringType tutoringType;
}
