package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "tuteeProfile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuteeProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private Location location;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Subject> subjectsNeeded = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TuteeGrade grade;

    private String description;

}
