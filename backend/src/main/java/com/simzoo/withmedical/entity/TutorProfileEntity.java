package com.simzoo.withmedical.entity;

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
            .nickname(this.member.getNickname())
            .gender(this.member.getGender())
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
}
