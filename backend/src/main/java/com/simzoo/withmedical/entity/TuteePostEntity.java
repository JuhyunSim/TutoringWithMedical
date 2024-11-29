package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.tuteePost.TuteePostingResponseDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.tuteePost.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.enums.TutoringType;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    @JoinColumn(name = "tuteeId")
    private TuteeProfileEntity tuteeProfile;
    private String description;
    @Enumerated(EnumType.STRING)
    private TutoringType type;
    private String possibleSchedule;
    private String level;
    private Integer fee;

    public TuteePostingResponseDto toResponseDto() {
        return TuteePostingResponseDto.builder()
            .postingId(this.id)
            .memberId(this.tuteeProfile.getMember().getId())
            .memberNickname(this.tuteeProfile.getMember().getNickname())
            .studentGrade(this.tuteeProfile.getGrade())
            .studentSchool(this.tuteeProfile.getSchool())
            .personality(this.tuteeProfile.getPersonality())
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
            .memberId(this.tuteeProfile.getMember().getId())
            .memberNickname(this.tuteeProfile.getMember().getNickname())
            .gender(this.tuteeProfile.getGender())
            .studentGrade(this.tuteeProfile.getGrade())
            .studentSchool(this.tuteeProfile.getSchool())
            .personality(this.tuteeProfile.getPersonality())
            .tutoringType(this.type)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .build();
    }

    public void saveTuteeProfile(TuteeProfileEntity tuteeProfile) {
        this.tuteeProfile = tuteeProfile;
    }

    public void update(UpdateTuteePostingRequestDto requestDto) {
        this.type = getUpdatedValue(requestDto.getTutoringType(), this.type);
        this.possibleSchedule = getUpdatedValue(requestDto.getPossibleSchedule(), this.possibleSchedule);
        this.description = getUpdatedValue(requestDto.getDescription(), this.description);
        this.fee = getUpdatedValue(requestDto.getFee(), this.fee);

        // 연관된 TuteeProfileEntity 업데이트
        if (this.tuteeProfile != null) {
            this.tuteeProfile.syncPost(this);
        }
    }

    private <T> T getUpdatedValue(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private Map<Long, TuteeProfileEntity> toMap(List<TuteeProfileEntity> tuteeProfileEntities) {

        if (tuteeProfileEntities == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PROFILE);
        }

        return tuteeProfileEntities.stream()
            .collect(Collectors.toMap(TuteeProfileEntity::getId, e -> e));
    }
}
