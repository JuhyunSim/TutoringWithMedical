package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.member.MemberResponseDto;
import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.service.SignupService;
import com.simzoo.withmedical.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private final SignupService signupService;
    private final MemberService memberService;
    private final VerificationService verificationService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupRequestDto requestDto) {

        return ResponseEntity.ok(signupService.signup(requestDto).toResponseDto());
    }

    /**
     * 인증번호 확인
     */
    @PostMapping("/signup/verify")
    public ResponseEntity<?> verify(@RequestParam String receivePhone,
        @RequestParam Integer verifyNumber) {

        verificationService.verifyNumber(receivePhone, verifyNumber);
        memberService.checkMemberExist(receivePhone);
        return ResponseEntity.ok().build();
    }
}
