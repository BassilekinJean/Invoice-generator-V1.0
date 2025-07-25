package com.bassilekin.report_generator.Services;

import org.springframework.stereotype.Service;

import com.bassilekin.report_generator.Model.Invoice;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public <T> byte[] generateInvoiceReport(
                        Invoice invoice,
                        String jasperPath, 
                        Map<String, Object> parameters
        ) throws JRException, FileNotFoundException {

        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
        
        // Utiliser des paramètres plutôt que DataSource pour les champs principaux
        if (parameters == null) {
            parameters = new HashMap<>();
        }
                
        // Créer une source de données pour les items si nécessaire
        //if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            JRBeanCollectionDataSource itemsDataSource = new JRBeanCollectionDataSource(invoice.getItems());
            parameters.put("TABLE_DATASET", itemsDataSource);
       // }
        
        // Utiliser l'objet Invoice comme source de données principale
        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(List.of(invoice));
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mainDataSource);

        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new JRException("Error generating PDF report", e);
        }
    }
}