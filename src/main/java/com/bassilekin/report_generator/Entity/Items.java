package com.bassilekin.report_generator.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor 
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;

    @Column(name = "description_produit")
    private String descriptionProduit;

    @Column(name = "unit_price_produit")
    private Double unitPriceProduit;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentLineItem> documentLineItems = new HashSet<>();

    public void addDocumentLineItem(DocumentLineItem documentLineItem) {
        documentLineItems.add(documentLineItem);
        documentLineItem.setItem(this);
    }

    public void removeDocumentLineItem(DocumentLineItem documentLineItem) {
        documentLineItems.remove(documentLineItem);
        documentLineItem.setItem(null);
    }

}