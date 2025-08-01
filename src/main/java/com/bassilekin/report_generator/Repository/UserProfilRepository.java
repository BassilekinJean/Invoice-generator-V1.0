package com.bassilekin.report_generator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bassilekin.report_generator.Model.UserProfils;

@Repository
public interface UserProfilRepository extends JpaRepository<UserProfils, Long> {
        
}
