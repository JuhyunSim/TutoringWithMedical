package com.simzoo.withmedical.util.validator;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ProfileNotNullValidator implements ConstraintValidator<ProfileNotNull, SignupRequestDto>{

    @Override
    public void initialize(ProfileNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(SignupRequestDto requestDto, ConstraintValidatorContext context) {
        if (requestDto.getRole() == Role.TUTOR) {
            return !Objects.isNull(requestDto.getTutorProfile());
        } else {
            return !Objects.isNull(requestDto.getTuteeProfile());
        }
    }
}