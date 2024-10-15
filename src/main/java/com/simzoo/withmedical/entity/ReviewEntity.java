package com.simzoo.withmedical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String content;
    private Integer rating;
}
