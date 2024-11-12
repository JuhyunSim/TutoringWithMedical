package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.MemberResponseDto;
import com.simzoo.withmedical.dto.UpdateMemberRequestDto;
import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(@LoginId Long userId) {
        log.info("userId = {}", userId);
        return ResponseEntity.ok(memberService.getMyInfo(userId).toResponseDto());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(@LoginId Long userId,
        @RequestBody UpdateMemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.updateMember(userId, requestDto).toResponseDto());
    }
}
