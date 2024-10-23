package com.simzoo.withmedical.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProfileNotNullValidator.class)
public @interface ProfileNotNull {

    String message() default "프로필 정보를 입력해주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
