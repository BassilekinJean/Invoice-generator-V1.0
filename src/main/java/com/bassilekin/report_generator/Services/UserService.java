package com.bassilekin.report_generator.Services;

import com.bassilekin.report_generator.DTOs.UserRegistrationDto;
import com.bassilekin.report_generator.Model.User;
import com.bassilekin.report_generator.Repository.UserRepository;
import com.bassilekin.report_generator.enums.AuthProvider;
import com.bassilekin.report_generator.enums.Role;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Tag(name = "User Service", description = "Logique Métier de gestion des utilisateurs")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(UserRegistrationDto registrationDto) {
       
        User newUser = new User();
        newUser.setUserEmail(registrationDto.userEmail());
        newUser.setUserPassword(passwordEncoder.encode(registrationDto.userPassword()));
        newUser.setProvider(AuthProvider.LOCAL);
        
        // temporairement, on attribue un rôle admin si l'email est
        if ("admin@gmail.com".equals(registrationDto.userEmail())) { 
            newUser.setRole(Role.ROLE_ADMIN); 
        } else {
            newUser.setRole(Role.ROLE_USER);
        }

        userRepository.save(newUser);
    }

    public User findUserWithEmail(String email){
         return userRepository.findByUserEmail(email);
    }
}