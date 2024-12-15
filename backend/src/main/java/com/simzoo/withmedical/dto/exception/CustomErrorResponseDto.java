package com.simzoo.withmedical.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomErrorResponseDto {
    private String errorCode;
    private String message;
}
