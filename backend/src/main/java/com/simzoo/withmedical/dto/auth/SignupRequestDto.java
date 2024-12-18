package com.simzoo.withmedical.dto.auth;

import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.util.AesUtil;
import com.simzoo.withmedical.util.validator.Password;
import com.simzoo.withmedical.util.validator.PasswordConfirm;
import com.simzoo.withmedical.util.validator.PhoneNumber;
import com.simzoo.withmedical.util.validator.ProfileNotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private String imageUrl;

    @NotBlank
    @PhoneNumber
    private String phoneNumber;

    @Password
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;
    private Role role;

    @Valid
    private TutorProfileRequestDto tutorProfile;
    @Valid
    private TuteeProfileRequestDto tuteeProfile;
    @Valid
    private List<TuteeProfileRequestDto> tuteeProfiles = new ArrayList<>();

    public MemberEntity toMemberEntity(PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
            .nickname(nickname)
            .gender(gender)
            .hashedPhoneNumber(AesUtil.generateHash(phoneNumber))
            .encryptedPhoneNumber(AesUtil.encrypt(phoneNumber))
            .password(passwordEncoder.encode(password))
            .roles(new ArrayList<>())
            .build();
    }
}
