package com.bassilekin.report_generator.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserProfilDto(

    @NotBlank(message = "Veuillez fournir un nom")
    String technicianName,

    String companyAddress,

    @Email(message = "Format d'email invalide")
    String companyEmail,

    @NotNull(message = "Le numéro de téléphone ne peut pas être vide")
    Number companyPhone

) {

}
