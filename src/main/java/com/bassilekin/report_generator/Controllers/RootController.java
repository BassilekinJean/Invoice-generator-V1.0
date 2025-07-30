package com.bassilekin.report_generator.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bassilekin.report_generator.DTOs.UserLoginDto;
import com.bassilekin.report_generator.Repository.UserRepository;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RootController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Bienvenue sur l'API de génération de rapports");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserLoginDto>> allUserEntity() {
        List<UserLoginDto> userList = userRepository.findAll()
            .stream()
            .map(user -> new UserLoginDto(user.getUserEmail(), user.getUserPassword()))
            .toList();

        return  ResponseEntity.ok(userList);
    }
    
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("API opérationnelle");
    }
}