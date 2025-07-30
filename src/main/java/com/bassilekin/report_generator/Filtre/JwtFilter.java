package com.bassilekin.report_generator.Filtre;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bassilekin.report_generator.Services.CustomUserDetailService;
import com.bassilekin.report_generator.configuration.JWTutils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final JWTutils jwTutils;
    private final CustomUserDetailService userDetailService;
    private final RequestMatcher publicEndpointsMatcher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                        @NonNull HttpServletResponse response, 
                                            @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        if (publicEndpointsMatcher.matches(request)) {
            filterChain.doFilter(request, response); 
            return; 
        }

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            if (token != null && !token.isEmpty()) {
                username = jwTutils.extractUsername(token);
            }
        }

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            response.getWriter().write("Jeton d'authentification manquant ou invalide.");
            return;
        }

        if (jwTutils.isTokenInvalidated(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            response.getWriter().write("Votre session a été déconnectée ou le jeton invalidé.");
            return; // Arrête la chaîne de filtres ici
        }

        if (token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwTutils.validateToken(token, username)) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username); 

                //Vérifier l'expiration après la blacklist
                if (jwTutils.isTokenExpired(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                    response.getWriter().write("Le jeton d'accès a expiré. Veuillez vous reconnecter.");
                    return; // Arrête la chaîne de filtres ici
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null,
                    userDetails.getAuthorities() 
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
