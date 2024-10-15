package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TuteePostingSimpleResponseDto {
    private Long postingId;
    private TuteeGrade studentGrade;
    private String studentSchool;
    private String personality;
    private TutoringType tutoringType;
    private String possibleSchedule;
    private String level;
    private Integer fee;
}
