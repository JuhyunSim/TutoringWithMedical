package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto.UpdateTuteeProfileRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileResponseDto;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.GradeType;
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
import jakarta.persistence.PostLoad;
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

    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuteeProfile")
    private List<SubjectEntity> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;

    @Enumerated(EnumType.STRING)
    private GradeType gradeGroup;

    private String personality;

    private String description;

    private String school;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuteeProfile")
    private List<TuteePostEntity> posts = new ArrayList<>();

    @PostLoad
    public void syncGradeGroup() {
        this.gradeGroup = this.getGrade().getGroup();
    }

    public void addSubject(List<SubjectEntity> subjects) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        this.subjects.clear();
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

    public void addPost(TuteePostEntity postEntity) {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(postEntity);
    }

    public void updateProfile(UpdateTuteeProfileRequestDto updateTuteeProfileRequestDto) {
        updateIfNotNull(updateTuteeProfileRequestDto.getTuteeName(), name -> this.name = name);
        updateIfNotNull(updateTuteeProfileRequestDto.getTuteeGrade(),
            tuteeGrade -> this.grade = tuteeGrade);
        updateIfNotNull(updateTuteeProfileRequestDto.getDescription(),
            description -> this.description = description);
        updateIfNotNull(updateTuteeProfileRequestDto.getLocation().getSigungu().getFull_addr(),
            location -> this.location = location);
    }

    public void syncPost(TuteePostEntity tuteePostEntity) {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }

        boolean exists = this.posts.stream()
            .anyMatch(e -> e.getId().equals(tuteePostEntity.getId()));

        if (!exists) {
            this.posts.add(tuteePostEntity);
        } else {
            this.posts = this.posts.stream()
                .map(e -> e.getId().equals(tuteePostEntity.getId()) ? tuteePostEntity : e)
                .toList();
        }

        if (this.member != null) {
            this.member.syncTutees(this);
        }
    }

    private <T> void updateIfNotNull(T value, Consumer<T> updater) {
        if (value != null) {
            updater.accept(value);
        }
    }
}
