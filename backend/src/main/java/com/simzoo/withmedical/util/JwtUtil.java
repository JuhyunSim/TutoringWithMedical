package com.simzoo.withmedical.util;

import com.simzoo.withmedical.enums.Role;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; //1시간

    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; //1주일

    public String generateAccessToken(String nickname, Long userId, Role role) {

        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        return createToken(nickname, userId, role, key, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String nickname, Long userId, Role role) {

        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        return createToken(nickname, userId, role, key, REFRESH_TOKEN_EXPIRATION);
    }

    public UserVo getUserVo(String token) {
        Claims claims = extractAll(token);
        return new UserVo(Long.valueOf(AesUtil.decrypt(claims.getId())),
            AesUtil.decrypt(claims.getSubject()));
    }

    public boolean validateToken(String token) throws CustomException {

        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        if (expiredToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException exception) {
            log.error(exception.getMessage(), exception);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return true;
    }

    public boolean expiredToken(String token) throws CustomException{
        if (new Date().after(extractAll(token).getExpiration())) {
            return true;
        }
        return false;
    }

    public long extractExpiration(String token) {
        return extractAll(token).getExpiration().getTime();
    }

    public Authentication getAuthentication(String token) {

        String nickname = AesUtil.decrypt(extractAll(token).getSubject());
        String role = extractRole(token);

        User userDetails = new User(nickname, "",
            List.of(new SimpleGrantedAuthority(role)));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String createToken(String nickname, Long userId, Role role, SecretKey key,
        Long expiration) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", AesUtil.encrypt(role.name()));

        return Jwts.builder()
            .subject(AesUtil.encrypt(nickname))
            .id(AesUtil.encrypt(userId.toString()))
            .claims(claims)
            .issuedAt(new Date())
            .expiration(Date.from(Instant.now().plusMillis(expiration)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims extractAll(String token) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String extractRole(String token) {
        return extractAll(token).get("role", String.class);
    }

}
