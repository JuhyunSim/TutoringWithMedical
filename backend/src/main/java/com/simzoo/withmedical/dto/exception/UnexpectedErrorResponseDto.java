package com.simzoo.withmedical.dto.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UnexpectedErrorResponseDto {
    private final String message;      // 에러 메시지
    private final String errorCode;    // 커스텀 에러 코드 (예: "INTERNAL_SERVER_ERROR")
    private final int status;          // HTTP 상태 코드 (예: 500)
    private final LocalDateTime timestamp; // 에러 발생 시간
    private final String debugId;      // 요청/디버깅 ID (optional)

    public static UnexpectedErrorResponseDto of(
        String message,
        String errorCode,
        int status
    ) {
        return UnexpectedErrorResponseDto.builder()
            .message(message)
            .errorCode(errorCode)
            .status(status)
            .timestamp(LocalDateTime.now())
            .build();
    }
}