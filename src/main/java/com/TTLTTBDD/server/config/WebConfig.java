package com.TTLTTBDD.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
                        .allowedOrigins("http://localhost:8080", "http://10.0.167.232:8080") // Chỉ định origin được phép
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các HTTP method cho phép
                        .allowedHeaders("*") // Chấp nhận mọi header
                        .allowCredentials(true); // Hỗ trợ gửi thông tin xác thực (cookie, token)
            }
        };
    }
}
