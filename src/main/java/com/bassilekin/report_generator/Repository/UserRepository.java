package com.bassilekin.report_generator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bassilekin.report_generator.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserEmail(String email);
}
