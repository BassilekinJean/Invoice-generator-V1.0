package com.bassilekin.report_generator.Controllers;

import com.bassilekin.report_generator.Model.Invoice;
import com.bassilekin.report_generator.Model.Item; 
import com.bassilekin.report_generator.Services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller 
@RequestMapping("/") 
@RequiredArgsConstructor
public class JasperReportController {

    @Autowired
    private final ReportService reportService;

    /**
     * Affiche le formulaire de génération de rapport.
     */
    @GetMapping("/generate-report-form") 
    public String showReportForm(Model model) {
        Invoice defaultInvoice = new Invoice();
        
       
        List<Item> defaultItems = Arrays.asList();
        defaultInvoice.setItems(defaultItems);

        model.addAttribute("invoice", defaultInvoice); 
        return "index";
    }

    /**
     * Génère le PDF à partir des données soumises par le formulaire.
     * Le fichier sera directement téléchargé par le navigateur.
     */
    @PostMapping("/generate-pdf") // Endpoint pour la soumission du formulaire
    public ResponseEntity<byte[]> generatePDFFromForm(@ModelAttribute Invoice invoice) {
        try {
            String jasperPath = new File("src/main/resources/Report/Invoice.jasper").getAbsolutePath();
            
            // Les paramètres pour le rapport (peut être vide ou contenir des logos, etc.)
            Map<String, Object> parameters = new HashMap<>(); 

            byte[] pdfData = reportService.generateInvoiceReport(invoice, jasperPath, parameters);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .contentType(MediaType.APPLICATION_PDF) 
                    .body(pdfData);

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport PDF: " + e.getMessage());
            e.printStackTrace(); // Pour le débogage
            
            return ResponseEntity.internalServerError()
                                 .body(("Erreur lors de la génération du rapport : " + e.getMessage()).getBytes());
        }
    }

}