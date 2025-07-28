package com.bassilekin.report_generator.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "document_line_item") 
@NoArgsConstructor
@AllArgsConstructor
public class DocumentLineItem {

    @EmbeddedId
    private DocumentLineItemId id;

    @ManyToOne
    @MapsId("reportId") 
    @JoinColumn(name = "id_report") 
    private Document document; 

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "id") 
    private Items item; 

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price_at_time")
    private Double unitPriceAtTime;

    @Column(name = "description_at_time")
    private String descriptionAtTime;

}