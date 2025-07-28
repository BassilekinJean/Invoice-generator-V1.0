package com.bassilekin.report_generator.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.bassilekin.report_generator.DTOs.UserRegistrationDto;
import com.bassilekin.report_generator.Entity.User;
import com.bassilekin.report_generator.Repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class InscriptionConnexionContoller {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> incriptionsUser(@Valid @RequestBody UserRegistrationDto registrationDto) {

        if (userRepository.findByUserEmail(registrationDto.userEmail()) != null) {
            return ResponseEntity.badRequest().body("Un utilisateur avec cet idantifiant existe déjà");
        }

        User newUser = new User();
        newUser.setUserName(registrationDto.userName());
        newUser.setUserAddress(registrationDto.userAddress());
        newUser.setUserPhone(registrationDto.userPhone());
        newUser.setUserEmail(registrationDto.userEmail());
        newUser.setUserPassword(passwordEncoder.encode(registrationDto.userPassword()));
        newUser.setRole("ROLE_USER"); 

        return ResponseEntity.ok(userRepository.save(newUser));

    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User entity) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(entity.getUserEmail(), entity.getUserPassword()));
            return ResponseEntity.ok("User logged in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usename ou mot de passe incorect");
        }

    }
    
}
