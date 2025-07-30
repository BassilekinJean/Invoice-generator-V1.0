package com.bassilekin.report_generator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bassilekin.report_generator.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserEmail(String email);
}
