package com.simzoo.withmedical.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponseDto {
    private String accessToken;
}
