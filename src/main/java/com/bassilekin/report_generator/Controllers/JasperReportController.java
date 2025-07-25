package com.bassilekin.report_generator.Controllers;

import com.bassilekin.report_generator.Model.Invoice;
import com.bassilekin.report_generator.Model.Item; // Assurez-vous d'importer Item
import com.bassilekin.report_generator.Services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // <-- Changer de @RestController à @Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // <-- Changement ici
@RequestMapping("/") // <-- Changer le mapping pour que le formulaire soit sur la racine ou un chemin simple
@RequiredArgsConstructor
public class JasperReportController {

    @Autowired
    private final ReportService reportService;

    /**
     * Affiche le formulaire de génération de rapport.
     */
    @GetMapping("/generate-report-form") // Endpoint pour afficher le formulaire
    public String showReportForm(Model model) {
        Invoice defaultInvoice = new Invoice();
        
        // Ajouter des items par défaut si désiré
        List<Item> defaultItems = Arrays.asList(
            
        );
        defaultInvoice.setItems(defaultItems);

        model.addAttribute("invoice", defaultInvoice); // Le nom de l'attribut doit correspondre à th:object
        return "index"; // Ceci correspondra à src/main/resources/templates/reportForm.html
    }

    /**
     * Génère le PDF à partir des données soumises par le formulaire.
     * Le fichier sera directement téléchargé par le navigateur.
     */
    @PostMapping("/generate-pdf") // Endpoint pour la soumission du formulaire
    public ResponseEntity<byte[]> generatePDFFromForm(@ModelAttribute Invoice invoice) { // <-- Changer @RequestBody à @ModelAttribute
        try {
            String jasperPath = new File("src/main/resources/Report/Invoice.jasper").getAbsolutePath();
            
            // Les paramètres pour le rapport (peut être vide ou contenir des logos, etc.)
            Map<String, Object> parameters = new HashMap<>(); 

            byte[] pdfData = reportService.generateInvoiceReport(invoice, jasperPath, parameters);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .contentType(MediaType.APPLICATION_PDF) // <-- Utiliser MediaType.APPLICATION_PDF
                    .body(pdfData);

        } catch (Exception e) {
            // Gérer les exceptions de manière plus conviviale pour l'utilisateur
            // Par exemple, rediriger vers une page d'erreur ou afficher un message sur le formulaire
            System.err.println("Erreur lors de la génération du rapport PDF: " + e.getMessage());
            e.printStackTrace(); // Pour le débogage
            
            // Pour l'instant, on renvoie une erreur HTTP 500
            return ResponseEntity.internalServerError()
                                 .body(("Erreur lors de la génération du rapport : " + e.getMessage()).getBytes());
        }
    }

}