package com.bassilekin.report_generator.Controllers;

import com.bassilekin.report_generator.Model.Invoice;
import com.bassilekin.report_generator.Services.ReportService;
import com.bassilekin.report_generator.configuration.JWTutils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invoice") 
@RequiredArgsConstructor
public class JasperReportController {

    @Autowired
    private final ReportService reportService;
    private final JWTutils jwtUtils;

    /**
     * Génère le PDF à partir des données soumises par le formulaire.
     * Le fichier sera directement téléchargé par le navigateur.
     */
    @PostMapping("/generate-pdf") // Endpoint pour la soumission du formulaire
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") 
    @ResponseBody
    public ResponseEntity<?> generatePDFFromForm(@RequestBody Invoice invoice, HttpServletRequest request){
        try {
            String jasperPath = new File("src/main/resources/Report/Invoice.jasper").getAbsolutePath();
            Map<String, Object> parameters = new HashMap<>();

            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            if (!jwtUtils.isTokenInvalidated(token)) {
                return ResponseEntity.status(401).body("Session expirée ou invalide");
            }

            String email = jwtUtils.extractUsername(token);

            byte[] pdfData = reportService.generateInvoiceReport(invoice, jasperPath, parameters, email);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfData);

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport PDF: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la génération du rapport : " + e.getMessage()).getBytes());
        }
    }
}