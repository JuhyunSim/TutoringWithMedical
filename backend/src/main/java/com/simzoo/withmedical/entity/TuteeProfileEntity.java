package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileResponseDto;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "tuteeProfile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
public class TuteeProfileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    private String name;

    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Location location;

    @OneToMany(fetch = FetchType.LAZY)
    private List<SubjectEntity> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;

    private String description;

    public void addSubject(List<SubjectEntity> subjects) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        this.getSubjects().addAll(subjects);
    }

    public TuteeProfileResponseDto toResponseDto() {
        return TuteeProfileResponseDto.builder()
            .tuteeId(id)
            .tuteeName(name)
            .gender(gender)
            .location(location)
            .subjects(subjects.stream().map(SubjectEntity::getSubject).toList())
            .tuteeGrade(grade)
            .description(description)
            .build();
    }

    public void updateProfile(UpdateTuteeProfileRequestDto updateTuteeProfileRequestDto) {
        updateIfNotNull(updateTuteeProfileRequestDto.getTuteeGrade(),
            tuteeGrade -> this.grade = tuteeGrade);
        updateIfNotNull(updateTuteeProfileRequestDto.getDescription(),
            description -> this.description = description);
        updateIfNotNull(updateTuteeProfileRequestDto.getLocation(),
            location -> this.location = location);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> updater) {
        if (value != null) {
            updater.accept(value);
        }
    }
}
