package com.simzoo.withmedical.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_POSTING(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String message;
}
