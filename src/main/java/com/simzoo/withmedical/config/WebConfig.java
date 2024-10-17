package com.simzoo.withmedical.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000, http://localhost:3001, http://localhost:3002,")  // 프론트엔드 주소
            .allowedMethods("*")  // 허용할 HTTP 메서드
            .allowedHeaders("*")  // 모든 헤더 허용
            .allowCredentials(true)  // 쿠키 허용 (필요할 경우)
            .maxAge(3600);  // Preflight 요청 캐시 시간 설정 (1시간)
    }

}
