package com.bassilekin.report_generator.Model;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {

    private String titre;
    private String refNumber;
    private Date dateCreation;
    private Date dueDate;
}
