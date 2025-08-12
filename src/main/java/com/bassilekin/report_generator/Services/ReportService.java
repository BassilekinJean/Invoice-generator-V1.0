package com.bassilekin.report_generator.Services;

import org.springframework.stereotype.Service;

import com.bassilekin.report_generator.DTOs.InvoiceDto;
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
                        InvoiceDto invoiceDto,
                        String jasperPath, 
                        Map<String, Object> parameters, String email
        ) throws JRException, FileNotFoundException {

        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
        
        // Utiliser des paramètres plutôt que DataSource pour les champs principaux
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        Invoice invoice = formaterDataSource(invoiceDto, email);

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

    private Invoice formaterDataSource(InvoiceDto invoiceDto, String email){
        User user = userRepository.findByUserEmail(email);
        Optional<UserProfils> userProfils = userProfilRepository.findById(user.getId());
        
        Invoice invoice = new Invoice();
        
        invoice.setClientAddress(invoiceDto.clientAddress());
        invoice.setClientPhone(invoiceDto.clientPhone());
        invoice.setClientEmail(invoiceDto.clientEmail());
        invoice.setClientName(invoiceDto.clientName());
        invoice.setTermesContrat(invoiceDto.termesContrat());
        invoice.setItems(invoiceDto.items());
        invoice.setTechnicianAddress(userProfils.get().getUserAddress());
        invoice.setTechnicianEmail(userProfils.get().getContactEmail());
        invoice.setTechnicianName(userProfils.get().getUserName());
        invoice.setTechnicianPhone(userProfils.get().getUserPhone());

        return invoice;
    }
    
}