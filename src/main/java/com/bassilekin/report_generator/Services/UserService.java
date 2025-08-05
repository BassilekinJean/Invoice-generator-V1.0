package com.bassilekin.report_generator.Services;

import com.bassilekin.report_generator.DTOs.UserRegistrationDto;
import com.bassilekin.report_generator.Model.User;
import com.bassilekin.report_generator.Model.UserProfils;
import com.bassilekin.report_generator.Repository.UserProfilRepository;
import com.bassilekin.report_generator.Repository.UserRepository;
import com.bassilekin.report_generator.enums.AuthProvider;
import com.bassilekin.report_generator.enums.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfilRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(UserRegistrationDto registrationDto) {
       
        User newUser = new User();
        newUser.setUserEmail(registrationDto.userEmail());
        newUser.setUserPassword(passwordEncoder.encode(registrationDto.userPassword()));
        newUser.setProvider(AuthProvider.LOCAL);
        
        if ("admin@gmail.com".equals(registrationDto.userEmail())) { 
            newUser.setRole(Role.ROLE_ADMIN); 
        } else {
            newUser.setRole(Role.ROLE_USER);
        }

        userRepository.save(newUser);

        // UserProfils userProfile = new UserProfils();
        // userProfile.setUser(savedUser);
        // userProfile.setFirstName(registrationDto.firstName());
        // userProfile.setLastName(registrationDto.lastName());
        // userProfile.setContactEmail(registrationDto.contactEmail());
        // userProfile.setUserAddress(registrationDto.userAddress());
        // userProfile.setUserPhone(registrationDto.userPhone());

        // userProfileRepository.save(userProfile);
    }

    public User findUserWithEmail(String email){
         return userRepository.findByUserEmail(email);
    }
}