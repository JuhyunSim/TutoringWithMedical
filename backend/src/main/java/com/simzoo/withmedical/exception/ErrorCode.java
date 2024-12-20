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
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    NOT_FOUND_CHATROOM_MEMBER(HttpStatus.NOT_FOUND, "해당 채팅방 사용자를 찾을 수 없습니다."),
    NOT_FOUND_PROFILE(HttpStatus.NOT_FOUND, "프로필을 찾을 수 없습니다."),
    NOT_FOUNT_SUBJECT(HttpStatus.NOT_FOUND, "해당 과목이 없습니다."),
    PROFILE_ROLE_NOT_MATCH(HttpStatus.BAD_REQUEST, "사용자의 역할과 프로필이 일치하지 않습니다."),
    ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입한 회원입니다."),
    VERIFY_NUMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    NOT_FOUND_VERIFY_NUMBER(HttpStatus.NOT_FOUND, "인증번호가 존재하지 않거나 만료되었습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "복호화 과정에서 문제가 발생했습니다."),
    ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "암호화 과정에서 문제가 발생했습니다."),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_REVIEW(HttpStatus.UNAUTHORIZED, "이미 작성한 리뷰가 있습니다."),
    TUTEE_PROFILE_CANNOT_EXCEED_ONE_PROFILE(HttpStatus.BAD_REQUEST, "학생은 프로필을 2개 이상 가질 수 없습니다."),
    INVALID_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 데이터입니다."),
    INVALID_DATA_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않는 데이터 요청입니다."),
    FAILED_OBTAIN_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.")
    ;

    private HttpStatus httpStatus;
    private String message;
}
