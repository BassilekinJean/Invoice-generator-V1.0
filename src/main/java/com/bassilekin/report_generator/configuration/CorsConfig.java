package com.bassilekin.report_generator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // Applique la configuration à tous les chemins de l'API
                .allowedOrigins("http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:8080") // Remplacez par les origines de votre frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                .allowedHeaders("*") // Autorise tous les headers
                .allowCredentials(true); // Permet l'envoi de cookies d'authentification (si utilisés, pas pour JWT dans localStorage)
    }
}