package com.bassilekin.report_generator.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        // Message d'erreur personnalisé
        String errorMessage = "Jeton d'authentification manquant ou invalide.";
        
        // Construction de l'URL de redirection vers l'endpoint du contrôleur
        String redirectUrl = String.format(
            "/error?statusCode=%d&errorMessage=%s",
            HttpServletResponse.SC_UNAUTHORIZED, // code 401
            URLEncoder.encode(errorMessage, StandardCharsets.UTF_8)
        );
        
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}