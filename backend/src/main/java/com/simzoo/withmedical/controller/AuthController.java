package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.auth.JwtResponseDto;
import com.simzoo.withmedical.dto.auth.LoginRequestDto;
import com.simzoo.withmedical.dto.member.ChangePasswordDto;
import com.simzoo.withmedical.service.AuthService;
import com.simzoo.withmedical.service.LogoutService;
import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.service.VerificationService;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final LogoutService logoutService;
    private final VerificationService verificationService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/login/verify")
    public ResponseEntity<?> verify(@RequestParam String receivePhone,
        @RequestParam Integer verifyNumber) {

        verificationService.verifyNumber(receivePhone, verifyNumber);
        memberService.checkMemberExist(receivePhone);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {

        logoutService.logout(token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> cancelAccount(@LoginId Long userId, @RequestBody String password) {

        authService.deleteMember(userId, password);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@LoginId Long userId,
        @RequestBody ChangePasswordDto requestDto) {

        authService.changeMyPassword(userId, requestDto);

        return ResponseEntity.ok().build();
    }
}
