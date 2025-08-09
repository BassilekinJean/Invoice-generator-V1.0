package com.bassilekin.report_generator.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bassilekin.report_generator.enums.AuthProvider;
import com.bassilekin.report_generator.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'email ne peut pas etre vide")
    @Email(message = "Format d'email invalide")
    private String userEmail;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe ne peut pas etre vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String userPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL; 

    private String providerId;

    @Column(nullable = false)
    private int failedAttempt = 0;

    private LocalDateTime lockTime;

    private boolean accountLocked = false; 

    @OneToMany(mappedBy = "user")
    private List<Item> itemsList = new ArrayList<>();
}