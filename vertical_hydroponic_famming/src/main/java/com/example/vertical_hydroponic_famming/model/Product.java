package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String type; // PRODUCE, SUPPLY, EQUIPMENT

    private String unitOfMeasure; // KG, LITRE, UNIT

    private BigDecimal unitPrice;

    private BigDecimal costPrice;

    private Double currentStock;
}
