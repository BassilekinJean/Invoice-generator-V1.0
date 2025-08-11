package com.bassilekin.report_generator.Model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest{

    @Email(message = "L'adresse e-mail doit être valide")
    @NotBlank(message = "L'adresse e-mail est requise")
    private String userEmail;
}
