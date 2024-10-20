package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.TuteePostEntity;
import com.simzoo.withmedical.enums.TuteeGrade;
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
    private TuteeGrade tuteeGrade;

    @Size(min = 4, max = 20)
    @NotBlank
    private String school;

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
            .school(this.school)
            .personality(this.personality)
            .type(this.tutoringType)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .grade(this.tuteeGrade)
            .build();
    }
}
