package com.bassilekin.report_generator.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "document") 
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Long idReport;

    @Column(name = "total_amount_report")
    private Double totalAmountReport;

    @Column(name = "title_report")
    private String titleReport;

    @Column(name = "type_document")
    private String typeDocument;

    @Column(name = "statut_document")
    private String statutDocument;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "date_emission_document")
    @Temporal(TemporalType.DATE)
    private Date dateEmissionDocument;

    @Column(name = "due_date_document")
    @Temporal(TemporalType.DATE)
    private Date dueDateDocument;

    @ManyToOne
    @JoinColumn(name = "id_user") 
    private User user; 

    @ManyToOne
    @JoinColumn(name = "id_client") 
    private Client client; 

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentLineItem> lineItems = new HashSet<>();
   
}