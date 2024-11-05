package com.simzoo.withmedical.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.simzoo.withmedical.dto.MemberResponseDto;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    private String password;
    private String passwordConfirm;
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "memberRoles", joinColumns = @JoinColumn(name = "memberId"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private TutorProfileEntity tutorProfile;

    @OneToOne(fetch = LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private TuteeProfileEntity tuteeProfile;

    @OneToMany(fetch = LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private List<TuteeProfileEntity> tuteeProfiles;

    public MemberResponseDto toResponseDto() {
        return MemberResponseDto.builder()
            .id(id)
            .nickname(nickname)
            .gender(gender)
            .phoneNumber(phoneNumber)
            .role(roles)
            .tutorProfile(tutorProfile)
            .tuteeProfiles(tuteeProfiles)
            .tuteeProfile(tuteeProfile)
            .build();
    }

    public void saveTuteeProfile(TuteeProfileEntity profile, Role role) {
        if (role != Role.TUTEE) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tuteeProfile = profile;
    }

    public void saveTutorProfile(TutorProfileEntity profile, Role role) {
        if (role != Role.TUTOR) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tutorProfile = profile;
    }

    public void addTutorProfile(TuteeProfileEntity profile, Role role) {
        if (role != Role.PARENT) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tuteeProfiles.add(profile);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void updateLastLogin(LocalDateTime now) {
        this.lastLogin = now;
    }
}
