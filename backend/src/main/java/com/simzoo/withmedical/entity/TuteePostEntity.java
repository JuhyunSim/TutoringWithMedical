package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.tuteePost.TuteePostingResponseDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.dto.tuteePost.UpdateTuteePostingRequestDto;
import com.simzoo.withmedical.enums.GradeType;
import com.simzoo.withmedical.enums.TuteeGrade;
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
import jakarta.persistence.PostLoad;
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
    @JoinColumn(name = "memberId")
    private MemberEntity member;
    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;
    private GradeType gradeGroup;
    private Long tuteeId;
    private String description;
    private String school;
    private String personality;
    @Enumerated(EnumType.STRING)
    private TutoringType type;
    private String possibleSchedule;
    private String level;
    private Integer fee;

    @PostLoad
    public void syncGradeGroup() {
        this.gradeGroup = this.grade.getGroup(); // grade와 group 동기화
    }

    public TuteePostingResponseDto toResponseDto() {
        return TuteePostingResponseDto.builder()
            .postingId(this.id)
            .memberId(this.member.getId())
            .memberNickname(this.member.getNickname())
            .studentGrade(toMap(this.member.getTuteeProfiles()).get(tuteeId).getGrade())
            .studentSchool(toMap(this.member.getTuteeProfiles()).get(tuteeId).getSchool())
            .personality(toMap(this.member.getTuteeProfiles()).get(tuteeId).getPersonality())
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
            .memberNickname(this.member.getNickname())
            .gender(toMap(this.member.getTuteeProfiles()).get(tuteeId).getGender())
            .studentGrade(toMap(this.member.getTuteeProfiles()).get(tuteeId).getGrade())
            .studentSchool(toMap(this.member.getTuteeProfiles()).get(tuteeId).getSchool())
            .personality(toMap(this.member.getTuteeProfiles()).get(tuteeId).getPersonality())
            .tutoringType(this.type)
            .possibleSchedule(this.possibleSchedule)
            .level(this.level)
            .fee(this.fee)
            .build();
    }

    public void saveProfileInfo(TuteeProfileEntity tuteeProfileEntity) {
        this.school = tuteeProfileEntity.getSchool();
        this.personality = tuteeProfileEntity.getPersonality();
        this.grade = tuteeProfileEntity.getGrade();
    }

    public void update(UpdateTuteePostingRequestDto requestDto) {
        this.type = getUpdatedValue(requestDto.getTutoringType(), this.type);
        this.possibleSchedule = getUpdatedValue(requestDto.getPossibleSchedule(), this.possibleSchedule);
        this.description = getUpdatedValue(requestDto.getDescription(), this.description);
        this.fee = getUpdatedValue(requestDto.getFee(), this.fee);
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
