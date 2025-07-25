package com.bassilekin.report_generator.Controllers;

import java.io.File;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bassilekin.report_generator.Model.Invoice;
import com.bassilekin.report_generator.Services.ReportService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class JasperReportController {
    // Assuming you have a service to handle the PDF generation
    private final ReportService reportService;

    @PostMapping("/export") 
    public ResponseEntity <byte[]> generatePDF(@RequestBody Invoice invoice) {
        try { 
        String jasperPath = new File("src/main/resources/Report/Invoice.jasper").getAbsolutePath();

        // Laisser le service gérer les paramètres et les données
        HashMap<String, Object> parameters = new HashMap<>();

        byte[] pdfData = reportService.generateInvoiceReport(invoice, jasperPath, parameters);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdfData);
        }catch (Exception e) {
                // Handle exceptions appropriately
                throw new RuntimeException("Erreur de génération", e);
        }
    }

    @GetMapping("/error")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    

}
