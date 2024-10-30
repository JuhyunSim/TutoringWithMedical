package com.simzoo.withmedical.dto.auth;

import com.simzoo.withmedical.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String phoneNumber;
    private String password;
    private Role role;
}
