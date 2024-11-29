package com.simzoo.withmedical.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.simzoo.withmedical.dto.member.MemberResponseDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto;
import com.simzoo.withmedical.entity.chat.ChatRoomMember;
import com.simzoo.withmedical.entity.payment.PaymentEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.util.AesUtil;
import jakarta.annotation.Nullable;
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
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String hashedPhoneNumber;
    private String encryptedPhoneNumber;
    private String password;
    private String passwordConfirm;
    private LocalDateTime lastLogin;
    private LocalDateTime passwordChangedAt;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "memberRoles", joinColumns = @JoinColumn(name = "memberId"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    @Nullable
    private TutorProfileEntity tutorProfile;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    @Nullable
    private List<TuteeProfileEntity> tuteeProfiles;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<ChatRoomMember> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<PaymentEntity> payments = new ArrayList<>();

    public MemberResponseDto toResponseDto() {
        return MemberResponseDto.builder()
            .id(id)
            .nickname(nickname)
            .gender(gender)
            .phoneNumber(AesUtil.decrypt(encryptedPhoneNumber))
            .role(roles)
            .tutorProfile(safeConvert(tutorProfile, TutorProfileEntity::toResponseDto))
            .tuteeProfiles(tuteeProfiles == null ? List.of()
                : tuteeProfiles.stream().map(TuteeProfileEntity::toResponseDto).toList())
            .build();
    }

    public void saveTuteeProfiles(List<TuteeProfileEntity> tuteeProfiles, Role role) {
        if (role != Role.PARENT && role != Role.TUTEE) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }

        if (role == Role.TUTEE && tuteeProfiles.size() > 1) {
            throw new CustomException(ErrorCode.TUTEE_PROFILE_CANNOT_EXCEED_ONE_PROFILE);
        }

        this.tuteeProfiles = tuteeProfiles;
    }

    public void saveTutorProfile(TutorProfileEntity profile, Role role) {
        if (role != Role.TUTOR) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        this.tutorProfile = profile;
    }

    public void addTuteeProfile(TuteeProfileEntity profile, Role role) {
        if (role != Role.PARENT) {
            throw new CustomException(ErrorCode.PROFILE_ROLE_NOT_MATCH);
        }
        if (this.tuteeProfiles == null) {
            this.tuteeProfiles = new ArrayList<>();
        }
        this.tuteeProfiles.add(profile);
    }

    public void removeTuteeProfile(TuteeProfileEntity profile) {
        if (this.tuteeProfiles == null || !this.tuteeProfiles.contains(profile)) {
            throw new CustomException(ErrorCode.INVALID_DATA);
        }
        this.tuteeProfiles.remove(profile);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void updateLastLogin(LocalDateTime now) {
        this.lastLogin = now;
    }

    public void updateInfo(UpdateMemberRequestDto requestDto) {
        updateIfNotNull(requestDto.getNickname(), nickname -> this.nickname = nickname);
        updateIfNotNull(requestDto.getGender(), gender -> this.gender = gender);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
        this.passwordChangedAt = LocalDateTime.now();
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private <T, R> R safeConvert(T value, Function<T, R> mapper) {
        return value == null ? null : mapper.apply(value);
    }

    public void syncTutees(TuteeProfileEntity tuteeProfileEntity) {
        if (tuteeProfiles == null) {
            this.tuteeProfiles = new ArrayList<>();
        }

        boolean exists = this.tuteeProfiles.stream()
            .anyMatch(e -> e.getId().equals(tuteeProfileEntity.getId()));

        if (!exists) {
            this.tuteeProfiles.add(tuteeProfileEntity);
        } else {
            this.tuteeProfiles = this.tuteeProfiles.stream()
                .map(e -> e.getId().equals(tuteeProfileEntity.getId()) ? tuteeProfileEntity : e)
                .toList();
        }
    }
}
