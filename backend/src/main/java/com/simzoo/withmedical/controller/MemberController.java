package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.member.MemberResponseDto;
import com.simzoo.withmedical.dto.member.UpdateMemberRequestDto;
import com.simzoo.withmedical.dto.tutee.TuteeProfileRequestDto;
import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.service.AuthService;
import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.util.JwtUtil;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    /**
     * 내 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(@LoginId Long userId) {
        log.info("userId = {}", userId);
        return ResponseEntity.ok(memberService.getMyInfo(userId).toResponseDto());
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/me")
    public ResponseEntity<MemberResponseDto> updateMyInfo(@LoginId Long userId,
        @RequestBody UpdateMemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.updateMember(userId, requestDto).toResponseDto());
    }

    /**
     * 프로필 추가
     */
    @PutMapping("/me/add-profile")
    public ResponseEntity<MemberResponseDto> addProfile(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody TuteeProfileRequestDto requestDto) {

        token = token.substring("Bearer ".length());
        Long userId = jwtUtil.getUserVo(token).getId();
        Role role = Role.valueOf(jwtUtil.getUserVo(token).getRole());

        return ResponseEntity.ok(
            memberService.addTuteeProfile(userId, role, requestDto).toResponseDto());
    }

    /**
     * 학생 프로필 수정
     */
    @PutMapping("/parent/tutee")
    public ResponseEntity<MemberResponseDto> updateProfile(@LoginId Long userId,
        @RequestBody UpdateMemberRequestDto requestDto) {

        return ResponseEntity.ok(
            memberService.updateParentProfile(userId, requestDto).toResponseDto());
    }

    @DeleteMapping("/parent/tutee/{tuteeId}")
    public ResponseEntity<Void> deleteProfile(@LoginId Long userId, @PathVariable Long tuteeId) {

        memberService.removeProfile(userId, tuteeId);

        return ResponseEntity.ok().build();
    }
}
