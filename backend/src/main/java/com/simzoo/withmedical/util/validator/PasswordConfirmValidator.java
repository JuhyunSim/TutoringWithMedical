package com.simzoo.withmedical.util.validator;

import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, SignupRequestDto> {

    @Override
    public void initialize(PasswordConfirm constraintAnnotation) {
    }

    @Override
    public boolean isValid(SignupRequestDto signupRequestDto, ConstraintValidatorContext context) {

        return signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm());
    }
}
