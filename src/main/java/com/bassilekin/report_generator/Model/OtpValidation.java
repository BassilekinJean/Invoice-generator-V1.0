package com.bassilekin.report_generator.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidation {

    @Email(message = "L'adresse e-mail doit être valide")
    @NotBlank(message = "L'adresse e-mail est requise")
    private String userEmail;

    @NotBlank(message = "Code OTP requis")
    @Size(min = 6, max = 6, message = "Le code OTP doit contenir exactement 6 chiffres")
    private String otp;
}
