package com.bassilekin.report_generator.Model;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @NotBlank(message = "Le nom du client est requis")
    private String clientName;

    @NotBlank(message = "L'adresse du client est requise")
    private String clientAddress;

    @NotBlank(message = "Le numéro de téléphone du client est requis")
    @Size(min = 9, max = 15, message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres")
    private String clientPhone;

    @Email(message = "L'email du client doit être valide")
    private String clientEmail;

    @NotBlank(message = "Le nom du technicien est requis")
    private String technicianName;

    private String technicianAddress;

    @NotBlank(message = "Le numéro de téléphone du technicien est requis")
    private String technicianPhone;

    @Email(message = "L'email du technicien doit être valide")
    private String technicianEmail;

    private String termesContrat;
    private List<Item> items; 
    
}
