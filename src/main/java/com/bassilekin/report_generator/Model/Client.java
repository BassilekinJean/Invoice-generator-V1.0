package com.bassilekin.report_generator.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    private String clientName;
    private String clientEmail;
    private String clientAddress;
    private String clientPhone;

}
