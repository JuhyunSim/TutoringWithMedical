package com.simzoo.withmedical.dto.member;

import com.simzoo.withmedical.util.validator.Password;
import com.simzoo.withmedical.util.validator.PasswordConfirm;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@PasswordConfirm
public class ChangePasswordDto {
    private String oldPassword;

    @Password
    private String newPassword;

    private String confirmPassword;
}
