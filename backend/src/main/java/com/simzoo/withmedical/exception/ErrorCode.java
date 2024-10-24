package com.simzoo.withmedical.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_POSTING(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    PROFILE_ROLE_NOT_MATCH(HttpStatus.BAD_REQUEST, "사용자의 역할과 프로필이 일치하지 않습니다."),
    ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입한 회원입니다."),
    VERIFY_NUMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    NOT_FOUND_VERIFY_NUMBER(HttpStatus.NOT_FOUND, "인증번호가 존재하지 않거나 만료되었습니다.")
    ;

    private HttpStatus httpStatus;
    private String message;
}
