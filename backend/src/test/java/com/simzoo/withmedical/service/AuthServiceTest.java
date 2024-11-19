package com.simzoo.withmedical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.simzoo.withmedical.dto.member.ChangePasswordDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.repository.member.MemberRepository;
import com.simzoo.withmedical.util.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;


    @Test
    void changePassword_success() {
        //given
        MemberEntity memberEntity = MemberEntity.builder()
            .id(1L)
            .nickname("nickname")
            .password("encodedOldPassword")
            .build();

        ChangePasswordDto requestDto = ChangePasswordDto.builder()
            .newPassword("newPassword")
            .oldPassword("oldPassword")
            .confirmPassword("newPassword")
            .build();

        when(memberRepository.findById(memberEntity.getId())).thenReturn(Optional.of(memberEntity));
        when(passwordEncoder.matches(requestDto.getOldPassword(), memberEntity.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(requestDto.getNewPassword())).thenReturn("encodedPassword");
        //when
        authService.changeMyPassword(memberEntity.getId(), requestDto);

        //then
        assertEquals("encodedPassword", memberEntity.getPassword());
    }

}