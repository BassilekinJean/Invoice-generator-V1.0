package com.bassilekin.report_generator.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company {

    private String companyName;
    private String companyPhone;
    private String companyAddress;
    private byte[] logo;
}
