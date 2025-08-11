package com.bassilekin.report_generator.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserProfilDto(

    @NotBlank(message = "Veuillez fournir un nom")
    String technicianName,

    String companyAddress,

    @Email(message = "Format d'email invalide")
    String companyEmail,

    @NotNull(message = "Le numéro de téléphone ne peut pas être vide")
    @Size(min = 10, max = 15, message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres")
    Number companyPhone

) {

}
