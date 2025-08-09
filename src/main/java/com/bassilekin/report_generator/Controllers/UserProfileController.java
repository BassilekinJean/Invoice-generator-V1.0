package com.bassilekin.report_generator.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bassilekin.report_generator.DTOs.UserProfilDto;
import com.bassilekin.report_generator.Services.UserProfilService;
import com.bassilekin.report_generator.configuration.JWTutils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfilService userProfilService;
    private final JWTutils jwtUtils;

    @PostMapping(path =  "/profile", consumes = "application/json")
    public ResponseEntity<?> configProfil(@Valid @RequestBody UserProfilDto userProfilDto,
                                                    HttpServletRequest request) 
    {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant ou invalide");
        }
        String token = authHeader.substring(7);
        if (jwtUtils.isTokenInvalidated(token)) {
            return ResponseEntity.status(401).body("Session expirée ou invalide"); 
        }
        if (userProfilDto.companyAddress() == null || userProfilDto.companyPhone() == null ||
            userProfilDto.technicianName() == null) 
        {
            return ResponseEntity.badRequest().body("Tous les champs sont requis");
        }

        String email = jwtUtils.extractUsername(token);

        userProfilService.createProfil(userProfilDto, email);
        
        return ResponseEntity.ok(userProfilDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfilDto userProfilDto, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        if (jwtUtils.isTokenInvalidated(token)) {
            return ResponseEntity.status(401).body("Session expirée ou invalide");
        }

        String email = jwtUtils.extractUsername(token);
        userProfilService.updateProfile(userProfilDto, email);
        return ResponseEntity.ok(userProfilDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant ou invalide");
        }
        
        String token = authHeader.substring(7);

        if (jwtUtils.isTokenInvalidated(token)) {
            return ResponseEntity.status(401).body("Session expirée ou invalide");
        }

        String email = jwtUtils.extractUsername(token);
        UserProfilDto userProfile = userProfilService.getUserProfile(email);
        if (userProfile == null) {
            return ResponseEntity.status(404).body("Profil non trouvé");
        }

        return ResponseEntity.ok(userProfile);
    }
    
    
}
