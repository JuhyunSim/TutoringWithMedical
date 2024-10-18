package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TuteePostingResponseDto {

    private Long postingId;
    private Long memberId;
    private String memberNickname;
    private TuteeGrade studentGrade;
    private String studentSchool;
    private String personality;
    private TutoringType tutoringType;
    private String possibleSchedule;
    private String level;
    private Integer fee;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
