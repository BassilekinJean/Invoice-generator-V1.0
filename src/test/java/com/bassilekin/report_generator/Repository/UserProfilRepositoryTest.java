package com.bassilekin.report_generator.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserProfilRepositoryTest {

    @Autowired
    private UserProfilRepository userProfilRepository;

    @Test
    void shouldReturnUser(){
        List<?> usersList = userProfilRepository.findAll();

        assertEquals(usersList, usersList);
        
    }
}
