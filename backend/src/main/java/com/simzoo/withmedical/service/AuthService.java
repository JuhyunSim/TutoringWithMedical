package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.auth.JwtResponseDto;
import com.simzoo.withmedical.dto.auth.LoginRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.util.AesUtil;
import com.simzoo.withmedical.util.JwtUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public JwtResponseDto login(LoginRequestDto requestDto) {

        String phoneNumber = requestDto.getPhoneNumber();
        String password = requestDto.getPassword();

        MemberEntity memberEntity = memberRepository.findByHashedPhoneNumber(
                AesUtil.generateHash(phoneNumber))
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (notMatchPassword(memberEntity, password, passwordEncoder)) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        memberEntity.updateLastLogin(LocalDateTime.now());

        return JwtResponseDto.builder().accessToken(
            jwtUtil.generateAccessToken(memberEntity.getNickname(), memberEntity.getId(),
                requestDto.getRole())).build();
    }

    private static boolean notMatchPassword(MemberEntity member, String password,
        PasswordEncoder passwordEncoder) {
        return !passwordEncoder.matches(password, member.getPassword());
    }
}
