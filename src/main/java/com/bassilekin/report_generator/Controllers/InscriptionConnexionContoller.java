package com.bassilekin.report_generator.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.bassilekin.report_generator.DTOs.UserLoginDto;
import com.bassilekin.report_generator.DTOs.UserRegistrationDto;
import com.bassilekin.report_generator.Entity.User;
import com.bassilekin.report_generator.Repository.UserRepository;
import com.bassilekin.report_generator.Services.LoginAttemptService;
import com.bassilekin.report_generator.configuration.JWTutils;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class InscriptionConnexionContoller {

    private final JWTutils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService; 
    private final AuthenticationManager authenticationManager;

    private final RateLimiter loginRateLimiter;
    private final RateLimiter registerRateLimiter;


    @PostMapping("/register")
    public ResponseEntity<?> incriptionsUser(@Valid @RequestBody UserRegistrationDto registrationDto) {

        try {
            // --- Apply Rate Limiting for Registration ---
            registerRateLimiter.acquirePermission(); 

            if (!registrationDto.userPassword().equals(registrationDto.confirmPassword())) {
                return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas.");
            }

            if (userRepository.findByUserEmail(registrationDto.userEmail()) != null) {
                return ResponseEntity.badRequest().body("Un utilisateur avec cet identifiant existe déjà");
            }

            User newUser = new User();
            newUser.setUserName(registrationDto.userName());
            newUser.setUserAddress(registrationDto.userAddress());
            newUser.setUserPhone(registrationDto.userPhone());
            newUser.setUserEmail(registrationDto.userEmail());
            newUser.setUserPassword(passwordEncoder.encode(registrationDto.userPassword()));
            newUser.setRole("ROLE_USER");

            return ResponseEntity.ok(userRepository.save(newUser));

        } catch (RequestNotPermitted e) {
            log.warn("Tentative d'enregistrement limitée pour l'email: {}", registrationDto.userEmail());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS) 
                    .body("Trop de tentatives d'enregistrement. Veuillez réessayer plus tard.");
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'utilisateur: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur inattendue est survenue.");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto loginDto, HttpServletResponse response) { 
        String userEmail = loginDto.username(); 
        try {       

            loginRateLimiter.acquirePermission();

            if (loginAttemptService.isAccountLocked(userEmail)) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Votre compte est temporairement bloqué en raison de trop de tentatives de connexion échouées. Veuillez réessayer plus tard.");
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, loginDto.password())
            );

            if (authentication.isAuthenticated()) {
                loginAttemptService.loginSucceeded(userEmail); 

                String accessToken = jwtUtils.generateToken(userEmail);
                String refreshToken = jwtUtils.generateRefreshToken(userEmail);

                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setSecure(true); 
                refreshTokenCookie.setPath("/auth/refresh");
                refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshTokenExpiration() / 1000)); 
                response.addCookie(refreshTokenCookie);

                Map<String, String> authData = new HashMap<>();
                authData.put("token", accessToken);
                authData.put("type", "Bearer");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom d'utilisateur ou mot de passe incorrect");

        } catch (RequestNotPermitted e) {
            log.warn("Tentative de connexion limitée pour l'email: {}", userEmail);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS) 
                    .body("Trop de tentatives de connexion. Veuillez réessayer plus tard.");

        } catch (AuthenticationException e) { 
            log.warn("Tentative de connexion échouée pour l'utilisateur {}: {}", userEmail, e.getMessage());
            loginAttemptService.loginFailed(userEmail); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom d'utilisateur ou mot de passe incorrect");

        } catch (Exception e) { 
            log.error("Erreur inattendue lors de la connexion de l'utilisateur {}: {}", userEmail, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur inattendue est survenue.");
        }
    }    

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || jwtUtils.isTokenExpired(refreshToken)) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jeton de rafraîchissement invalide ou expiré.");
        }

        try {
            String username = jwtUtils.extractUsername(refreshToken);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jeton de rafraîchissement invalide.");
            }

            String newAccessToken = jwtUtils.generateToken(username);

            Map<String, String> authData = new HashMap<>();
            authData.put("accessToken", newAccessToken);
            authData.put("type", "Bearer");
            return ResponseEntity.ok(authData);

        } catch (Exception e) {
            log.error("Erreur lors du rafraîchissement du jeton: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec du rafraîchissement du jeton.");
        }

    }

}
