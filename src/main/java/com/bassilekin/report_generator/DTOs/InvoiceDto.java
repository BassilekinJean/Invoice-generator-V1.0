package com.bassilekin.report_generator.DTOs;

import java.util.List;

import com.bassilekin.report_generator.Model.Item;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InvoiceDto(
    @NotBlank(message = "Le nom du client est requis")
    String clientName,

    @NotBlank(message = "L'adresse du client est requise")
    String clientAddress,

    @NotBlank(message = "Le numéro de téléphone du client est requis")
    @Size(min = 9, max = 15, message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres")
    String clientPhone,

    @Email(message = "L'email du client doit être valide")
    String clientEmail,

    String termesContrat,
    List<Item> items 

) {

}
