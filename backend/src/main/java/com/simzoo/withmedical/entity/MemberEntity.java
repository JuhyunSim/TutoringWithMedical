package com.simzoo.withmedical.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.simzoo.withmedical.dto.MemberResponseDto;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    private String password;
    private String passwordConfirm;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private TutorProfileEntity tutorProfile;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private TuteeProfileEntity tuteeProfile;

    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private List<TuteeProfileEntity> tuteeProfiles;

    public MemberResponseDto toResponseDto() {
        return MemberResponseDto.builder()
            .id(id)
            .nickname(nickname)
            .gender(gender)
            .phoneNumber(phoneNumber)
            .role(role)
            .tutorProfile(tutorProfile)
            .tuteeProfiles(tuteeProfiles)
            .tuteeProfile(tuteeProfile)
            .build();
    }

    public void saveTuteeProfile(TuteeProfileEntity profile) {
        if (role == Role.TUTEE) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tuteeProfile = profile;
    }

    public void saveTutorProfile(TutorProfileEntity profile) {
        if (role == Role.TUTOR) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tutorProfile = profile;
    }

    public void addTutorProfile(TuteeProfileEntity profile) {
        if (role == Role.PARENT) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tuteeProfiles.add(profile);
    }
}
