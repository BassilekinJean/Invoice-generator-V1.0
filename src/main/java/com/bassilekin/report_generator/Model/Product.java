package com.bassilekin.report_generator.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String productName;
    private double productPrice;
    private Integer productQuantity;
    private String productDescription;

}