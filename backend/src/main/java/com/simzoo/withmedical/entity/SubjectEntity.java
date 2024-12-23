package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.enums.Subject;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class SubjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tuteeId")
    private TuteeProfileEntity tuteeProfile;

    @ManyToOne
    @JoinColumn(name = "tutorId")
    private TutorProfileEntity tutorProfile;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    public static SubjectEntity of(Subject subject, TutorProfileEntity tutorProfile) {
        return SubjectEntity.builder()
            .subject(subject)
            .tutorProfile(tutorProfile)
            .build();
    }

    public static SubjectEntity of(Subject subject, TuteeProfileEntity tuteeProfile) {
        return SubjectEntity.builder()
            .subject(subject)
            .tuteeProfile(tuteeProfile)
            .build();
    }
}
