package com.bassilekin.report_generator.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportData {
    private String clientName;
    private String clientAddress;
    private String clientPhone;
    private String clientEmail;
    private String technicianName;
    private String technicianAddress;
    private String technicianPhone;
    private String technicianEmail;
    private String termesContrat;
    private List<Item> items;

}
