package com.bassilekin.report_generator.Filtre;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                        @NonNull HttpServletResponse response, 
                                            @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwTutils.extractUsername(token);
        }

        if (token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwTutils.validateToken(token, username)) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username); 
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
