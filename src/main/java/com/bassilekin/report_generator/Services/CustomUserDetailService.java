package com.bassilekin.report_generator.Services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bassilekin.report_generator.Entity.User;
import com.bassilekin.report_generator.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

    private final UserRepository userRepository; 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(username);

        if (user ==null) {
            throw new UsernameNotFoundException("Utilisateur non Trouvé avec Email" + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(), 
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
    }

}
