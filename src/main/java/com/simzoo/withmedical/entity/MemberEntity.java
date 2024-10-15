package com.simzoo.withmedical.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    private String password;
    private String passwordConfirm;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "member", fetch = LAZY)
    private TutorProfileEntity tutorProfile;
    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<TuteeProfileEntity> tuteeProfile;
}
