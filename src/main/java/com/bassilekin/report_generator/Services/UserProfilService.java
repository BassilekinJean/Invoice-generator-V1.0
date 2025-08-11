package com.bassilekin.report_generator.Services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bassilekin.report_generator.DTOs.UserProfilDto;
import com.bassilekin.report_generator.Model.User;
import com.bassilekin.report_generator.Model.UserProfils;
import com.bassilekin.report_generator.Repository.UserProfilRepository;
import com.bassilekin.report_generator.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfilService {

    @Autowired
    private final UserProfilRepository userProfilRepository;

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public void createProfil(UserProfilDto userProfilDto, String email){
        User user = userRepository.findByUserEmail(email);

        // Charge le profil existant ou crée-le si absent
        Optional<UserProfils> existingProfil = userProfilRepository.findById(user.getId());
        UserProfils userProfils;
        if (existingProfil.isPresent()) {
            userProfils = existingProfil.get();
        } else {
            userProfils = new UserProfils();
            userProfils.setUser(user);       // relation
        }

        userProfils.setContactEmail(userProfilDto.companyEmail());
        userProfils.setUserAddress(userProfilDto.companyAddress());
        userProfils.setUserPhone(userProfilDto.companyPhone());
        userProfils.setUserName(userProfilDto.technicianName());

        userProfilRepository.save(userProfils);
    }

    public void updateProfile(UserProfilDto userProfilDto, String email){

        User user = userRepository.findByUserEmail(email);

        Optional<UserProfils> existingProfil = userProfilRepository.findById(user.getId());

        UserProfils userProfils;
        
        if (existingProfil.isPresent()) {
            userProfils = existingProfil.get();
        } else {
            userProfils = new UserProfils();
            userProfils.setUser(user);      
        }

        userProfils.setContactEmail(userProfilDto.companyEmail());
        userProfils.setUserAddress(userProfilDto.companyAddress());
        userProfils.setUserPhone(userProfilDto.companyPhone());
        userProfils.setUserName(userProfilDto.technicianName());

        userProfilRepository.save(userProfils);
    }

    @Transactional(readOnly = true)
    public UserProfilDto getUserProfile(String email) {

        User user = userRepository.findByUserEmail(email);

        return userProfilRepository.findById(user.getId()).map(userProfil -> {

            UserProfilDto dto = new UserProfilDto(userProfil.getUserName(),
                    userProfil.getUserAddress(),
                    userProfil.getContactEmail(),
                    userProfil.getUserPhone());
            return dto;
        }).orElse(null);
    }

    public boolean isProfileConfigured(String email) {
        User user = userRepository.findByUserEmail(email);
        Optional<UserProfils> userProfile = userProfilRepository.findById(user.getId());
        
        // Vérifie si le profil existe et s'il contient les informations nécessaires
        return userProfile.isPresent() && 
               userProfile.get().getUserAddress() != null &&
               userProfile.get().getUserPhone() != null &&
               userProfile.get().getUserName() != null;
    }
}
