package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.MemberResponseDto;
import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupRequestDto requestDto) {

        return ResponseEntity.ok(signupService.signup(requestDto).toResponseDto());
    }

}
