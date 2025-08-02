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

        // Si l'URL est publique, on passe au filtre suivant sans authentification
        if (publicEndpointsMatcher.matches(request)) {
            filterChain.doFilter(request, response); 
            return; 
        }

        final String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwTutils.extractUsername(token);

        // On vérifie que le token est valide et que l'utilisateur n'est pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // On vérifie les invalidations avant la validation finale
            if (jwTutils.isTokenInvalidated(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // On charge les détails de l'utilisateur et on valide le token
            UserDetails userDetails = userDetailService.loadUserByUsername(username); 

            // Si le token est valide et non expiré
            if (jwTutils.validateToken(token, userDetails) && !jwTutils.isTokenExpired(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null,
                    userDetails.getAuthorities() 
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // On continue la chaîne de filtres. Si le token n'était pas valide,
        // le contexte de sécurité est vide et l'AuthenticationEntryPoint sera déclenché.
        filterChain.doFilter(request, response);
    }
}
