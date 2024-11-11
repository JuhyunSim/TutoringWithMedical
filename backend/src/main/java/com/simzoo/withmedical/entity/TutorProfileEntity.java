package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.UpdateMemberRequestDto.UpdateTutorProfileRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.University;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "tutorProfile")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class TutorProfileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @OneToMany(fetch = FetchType.LAZY)
    private List<SubjectEntity> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Location location;

    @Enumerated(EnumType.STRING)
    private University university;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private String description;

    public TutorProfileResponseDto toResponseDto() {

        return TutorProfileResponseDto.builder()
            .tutorId(id)
            .subjects(this.subjects.stream().map(SubjectEntity::getSubject).toList())
            .location(this.location)
            .university(this.university)
            .status(this.status)
            .description(this.description)
            .build();
    }

    public void addSubjects(List<SubjectEntity> subjects) {
        this.getSubjects().addAll(subjects);
    }

    //Todo MultipartFile
    public void updateProfile(UpdateTutorProfileRequestDto updateTutorProfileRequestDto) {

        updateIfNotNull(updateTutorProfileRequestDto.getLocation(), location -> this.location = location);

        updateIfNotNull(updateTutorProfileRequestDto.getDescription(), description -> this.description = description);

        updateIfNotNull(updateTutorProfileRequestDto.getUniversity(), university -> this.university = university);

        updateIfNotNull(updateTutorProfileRequestDto.getStatus(), status -> this.status = status);
    }

    public void updateSubjects(List<SubjectEntity> newSubjects) {
        this.subjects.clear();
        this.subjects.addAll(newSubjects);
    }

    private <T> void updateIfNotNull(T value, java.util.function.Consumer<T> updater) {
        if (value != null) {
            updater.accept(value);
        }
    }
}
