package com.simzoo.withmedical.config;

import com.simzoo.withmedical.security.JwtAuthenticationFilter;
import com.simzoo.withmedical.service.LogoutService;
import com.simzoo.withmedical.util.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final LogoutService logoutService;

    private static final List<String> PERMIT_ALL_URLS = List.of(
        "/index.html", "/css/**", "/images/**", "/js/**", "/swagger-ui/*", "v3/api-docs/**",
        "/", "/auth/login/**", "/send-one", "/signup/**"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
            authorizeHttpRequests(
                request -> request
                    .requestMatchers(PERMIT_ALL_URLS.toArray(new String[0]))
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/tutor")
                    .permitAll()
                    .anyRequest().permitAll()
            )
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, logoutService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
