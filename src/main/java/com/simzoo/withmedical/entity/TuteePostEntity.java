package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.TuteePostingResponseDto;
import com.simzoo.withmedical.dto.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "tuteePost")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@AuditOverride(forClass = BaseEntity.class)
public class TuteePostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity member;
    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;
    private Long tuteeId;
    private String description;
    private String school;
    private String personality;
    @Enumerated(EnumType.STRING)
    private TutoringType type;
    private String possibleSchedule;
    private String level;
    private Integer fee;

    public TuteePostingResponseDto toResponseDto() {
        return TuteePostingResponseDto.builder()
            .postingId(this.id)
            .studentGrade(this.grade)
            .studentSchool(this.school)
            .personality(this.personality)
            .tutoringType(this.type)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .description(this.description)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }

    public TuteePostingSimpleResponseDto toSimpleResponseDto() {
        return TuteePostingSimpleResponseDto.builder()
            .postingId(this.id)
            .memberId(this.member.getId())
            .studentGrade(this.grade)
            .studentSchool(this.school)
            .personality(this.personality)
            .tutoringType(this.type)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .build();
    }

    public void update(UpdateTuteePostingRequestDto requestDto) {
        this.personality = getUpdatedValue(requestDto.getPersonality(), this.personality);
        this.type = getUpdatedValue(requestDto.getTutoringType(), this.type);
        this.possibleSchedule = getUpdatedValue(requestDto.getPossibleSchedule(), this.possibleSchedule);
        this.description = getUpdatedValue(requestDto.getDescription(), this.description);
        this.fee = getUpdatedValue(requestDto.getFee(), this.fee);
    }

    private <T> T getUpdatedValue(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}
