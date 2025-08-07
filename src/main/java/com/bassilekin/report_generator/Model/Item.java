package com.bassilekin.report_generator.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Le nom/description de l'article ne peut etre vide")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "La quantité ne peut etre null")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Veuillez définir un prix")
    @Column(nullable = false)
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;
}
