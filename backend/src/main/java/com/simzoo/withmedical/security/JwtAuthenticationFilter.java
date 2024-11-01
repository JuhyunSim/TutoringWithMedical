package com.simzoo.withmedical.security;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.service.LogoutService;
import com.simzoo.withmedical.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final String ACCESS_TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final LogoutService logoutService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);
        log.info("request path: {}", request.getRequestURI());
        if (accessToken != null) {

            if (jwtUtil.expiredToken(accessToken)) {
                throw new CustomException(ErrorCode.TOKEN_EXPIRED);
            }

            if (logoutService.isLoggedOut(accessToken)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
            log.info("authentication: {}, {}", authentication.getPrincipal(),
                authentication.getAuthorities());

            log.debug("nickname: {}, loginId: {}", jwtUtil.getUserVo(accessToken).getNickname(), jwtUtil.getUserVo(accessToken).getId());
            request.setAttribute("nickname", jwtUtil.getUserVo(accessToken).getNickname());
            request.setAttribute("loginId", jwtUtil.getUserVo(accessToken).getId());
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String token = request.getHeader(ACCESS_TOKEN_HEADER);


        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
