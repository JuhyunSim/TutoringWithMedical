package com.simzoo.withmedical.dto.tuteePost;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.enums.TutoringType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CreateTuteePostingRequestDto {

    @NotNull
    private Long tuteeId;

    @Size(max = 100)
    private String personality;

    @NotNull
    private TutoringType tutoringType;

    @Size(min = 3, max = 100)
    @NotBlank
    private String possibleSchedule;

    @Size(max = 100)
    private String level;

    @Min(1)
    @Max(10000)
    private Integer fee;

    private String description;

    public TuteePostEntity toEntity(MemberEntity member) {

        return TuteePostEntity.builder()
            .member(member)
            .description(this.description)
            .school(null)
            .personality(this.personality)
            .type(this.tutoringType)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .grade(null)
            .build();
    }
}
