package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.enums.TutoringType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@AuditOverride(forClass = BaseEntity.class)
public class TuteePostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long tuteeId;
    private String title;
    private String school;
    private String personality;
    @Enumerated(EnumType.STRING)
    private TutoringType type;
    private String possibleSchedule;
    private String level;
    private Long fee;
}
