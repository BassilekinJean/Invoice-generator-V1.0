package com.bassilekin.report_generator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.bassilekin.report_generator.Filtre.JwtFilter;
import com.bassilekin.report_generator.Security.CustomAuthenticationEntryPoint;
import com.bassilekin.report_generator.Services.CustomUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JWTutils jwtUtils;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtils, customUserDetailService, publicEndpointsMatcher());
    }

    @Bean
    public RequestMatcher publicEndpointsMatcher() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/login.html"), 
            new AntPathRequestMatcher("/gifs.gif"),
            new AntPathRequestMatcher("/register.html"), 
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/actuator/health")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(publicEndpointsMatcher()).permitAll() 
                    .requestMatchers("/error.html", "/images/**", "/").permitAll()
                    .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                    
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.sendRedirect("/error?code=403&message=" + accessDeniedException.getMessage());
                    })
                    .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Pour H2 console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
        
        return authenticationManagerBuilder.build();
    }
}
