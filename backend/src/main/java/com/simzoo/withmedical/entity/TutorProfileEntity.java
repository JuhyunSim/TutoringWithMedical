package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTutorProfileRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Subject;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    private String imageUrl;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tutorProfile")
    private List<SubjectEntity> subjects = new ArrayList<>();

    private String location;

    private String univName;

    private String univNumber;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private String description;

    public TutorProfileResponseDto toResponseDto() {

        return TutorProfileResponseDto.builder()
            .tutorId(id)
            .nickname(this.getMember().getNickname())
            .gender(this.getMember().getGender().getDescription())
            .imageUrl(imageUrl)
            .subjects(this.subjects.stream().map(SubjectEntity::getSubject).map(
                Subject::getDescription).toList())
            .location(this.location)
            .univName(this.univName)
            .status(this.status.getDescription())
            .description(this.description)
            .build();
    }

    public void addSubjects(List<SubjectEntity> subjects) {
        this.getSubjects().addAll(subjects);
    }

    public void updateProfile(UpdateTutorProfileRequestDto updateTutorProfileRequestDto) {

        updateIfNotNull(updateTutorProfileRequestDto.getImageUrl(), imageUrl -> this.imageUrl = imageUrl);

        updateIfNotNull(updateTutorProfileRequestDto.getLocation(), location -> this.location = location.getSigungu().getFull_addr());

        updateIfNotNull(updateTutorProfileRequestDto.getDescription(), description -> this.description = description);

        updateIfNotNull(updateTutorProfileRequestDto.getUniversity().getSchoolName(), univName -> this.univName = univName);

        updateIfNotNull(updateTutorProfileRequestDto.getUniversity().getSeq(), univNumber -> this.univNumber = univNumber);

        updateIfNotNull(updateTutorProfileRequestDto.getStatus(), status -> this.status = status);
    }

    public void updateSubjects(List<SubjectEntity> newSubjects) {
        this.subjects.clear();
        this.subjects.addAll(newSubjects);
    }

    public void saveMember(MemberEntity member) {
        this.member = member;
    }

    private <T> void updateIfNotNull(T value, java.util.function.Consumer<T> updater) {
        if (value != null) {
            updater.accept(value);
        }
    }
}
