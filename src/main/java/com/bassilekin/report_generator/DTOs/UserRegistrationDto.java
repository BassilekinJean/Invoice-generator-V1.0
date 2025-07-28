package com.bassilekin.report_generator.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
    Long id,

    @NotBlank(message = "Le nom ne peut pas etre vide")
    String userName,

    String userAddress,

    @NotNull
    Number userPhone,

    @NotBlank(message = "L'email ne peut etre vide")
    @Email(message = "Format d'email invalide")
    String userEmail,

    @NotBlank(message = "Le mot de passe ne peut etre vide")
        @Size(min = 8, message = "Au moins 8 caractère pour le mot de passe")
    String userPassword
) {

}
