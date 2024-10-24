package com.simzoo.withmedical.dto.auth;

import com.simzoo.withmedical.dto.TuteeProfileRequestDto;
import com.simzoo.withmedical.dto.TutorProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.util.validator.Password;
import com.simzoo.withmedical.util.validator.PasswordConfirm;
import com.simzoo.withmedical.util.validator.PhoneNumber;
import com.simzoo.withmedical.util.validator.ProfileNotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordConfirm
@ProfileNotNull
public class SignupRequestDto {

    @Size(min = 2, max = 20)
    @NotBlank
    private String nickname;

    @NotNull
    private Gender gender;

    @NotBlank
    @PhoneNumber
    private String phoneNumber;

    @Password
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;
    private Role role;

    private TutorProfileRequestDto tutorProfile;
    private TuteeProfileRequestDto tuteeProfile;
    private List<TuteeProfileRequestDto> tuteeProfiles = new ArrayList<>();

    public MemberEntity toMemberEntity() {
        return MemberEntity.builder()
            .nickname(nickname)
            .gender(gender)
            .phoneNumber(phoneNumber)
            .password(password)
            .role(role)
            .build();
    }


}