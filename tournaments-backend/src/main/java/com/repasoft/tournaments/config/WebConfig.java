package com.repasoft.tournaments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Povolit CORS pro všechny API endpointy
                .allowedOrigins("http://localhost:3000") // Povolit požadavky z frontendového serveru
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Povolit požadované metody
                .allowedHeaders("*") // Povolit všechny hlavičky
                .allowCredentials(true); // Povolit odesílání cookies
    }
}
