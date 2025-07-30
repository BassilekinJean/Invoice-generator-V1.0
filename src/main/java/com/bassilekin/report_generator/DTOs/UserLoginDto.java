package com.bassilekin.report_generator.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
    @NotBlank(message = "Username manquant")
    @Email(message = "Format d'email invalide")
    String username,

    @NotBlank(message = "Mot de passe manquant")
    @Size(min = 8, message = "Au moins 8 caractère pour le mot de passe")
    String password
) {

}
