package com.bassilekin.report_generator.Services;

import org.springframework.stereotype.Service;

import com.bassilekin.report_generator.Model.Invoice;
import com.bassilekin.report_generator.Model.User;
import com.bassilekin.report_generator.Model.UserProfils;
import com.bassilekin.report_generator.Repository.UserProfilRepository;
import com.bassilekin.report_generator.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserProfilRepository userProfilRepository;
    private final UserRepository userRepository;

    public <T> byte[] generateInvoiceReport(
                        Invoice invoice,
                        String jasperPath, 
                        Map<String, Object> parameters, String email
        ) throws JRException, FileNotFoundException {

        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
        
        // Utiliser des paramètres plutôt que DataSource pour les champs principaux
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        
        formaterDataSource(invoice, email);

        JRBeanCollectionDataSource itemsDataSource = new JRBeanCollectionDataSource(invoice.getItems());
        parameters.put("TABLE_DATASET", itemsDataSource);
       
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

    private void formaterDataSource(Invoice invoice, String email){
        User user = userRepository.findByUserEmail(email);
        Optional<UserProfils> userProfils = userProfilRepository.findById(user.getId());
        
        invoice.setTechnicianAddress(userProfils.get().getUserAddress());
        invoice.setTechnicianEmail(userProfils.get().getContactEmail());
        invoice.setTechnicianName(userProfils.get().getUserName());
        invoice.setTechnicianPhone(userProfils.get().getUserPhone());
    }
    
}