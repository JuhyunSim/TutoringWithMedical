package com.simzoo.withmedical.dto.tuteePost;

import com.simzoo.withmedical.enums.TutoringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTuteePostingRequestDto {
    private TutoringType tutoringType;
    private String possibleSchedule;
    private String description;
    private Integer fee;
}
