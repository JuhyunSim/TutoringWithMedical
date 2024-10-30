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

        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        log.info("authentication: {}, {}", authentication.getPrincipal(),
            authentication.getAuthorities());

        request.setAttribute("nickname", jwtUtil.getUserVo(accessToken).getNickname());
        request.setAttribute("loginId", jwtUtil.getUserVo(accessToken).getId());
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String token = request.getHeader(ACCESS_TOKEN_HEADER);

        if (token == null && logoutService.isLoggedOut(token)) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED_USER);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        token = token.replace(TOKEN_PREFIX, "");
        jwtUtil.validateToken(token);

        return token;
    }
}
