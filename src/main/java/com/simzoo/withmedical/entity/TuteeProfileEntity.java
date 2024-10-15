package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    private Location location;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tutee_subjects", joinColumns = @JoinColumn(name = "tuteeProfileId"))
    @Column(name = "subjectsNeeded")
    private List<Subject> subjectsNeeded = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;

    private String description;

}
