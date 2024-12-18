package com.simzoo.withmedical.util.validator;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ProfileNotNullValidator implements
    ConstraintValidator<ProfileNotNull, SignupRequestDto> {

    @Override
    public void initialize(ProfileNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(SignupRequestDto requestDto, ConstraintValidatorContext context) {

        boolean isTutorProfileInvalid = Objects.isNull(requestDto.getTutorProfile());
        boolean isTuteeProfileInvalid = Objects.isNull(requestDto.getTuteeProfile());
        boolean isTuteeProfilesEmpty =
            requestDto.getTuteeProfiles() == null || requestDto.getTuteeProfiles().isEmpty();

        boolean isTutorProfileValid = !isTutorProfileInvalid;
        boolean isTuteeProfileValid = !isTuteeProfileInvalid;
        boolean isTuteeProfilesNotEmpty = !isTuteeProfilesEmpty;

        if (isTuteeProfileInvalid && isTuteeProfilesEmpty && isTutorProfileInvalid) {
            return false;
        }

        if (requestDto.getRole() == Role.TUTOR) {
            return isTutorProfileValid;
        } else if (requestDto.getRole() == Role.TUTEE) {
            return isTuteeProfileValid;
        } else {
            return isTuteeProfilesNotEmpty;
        }
    }
}
