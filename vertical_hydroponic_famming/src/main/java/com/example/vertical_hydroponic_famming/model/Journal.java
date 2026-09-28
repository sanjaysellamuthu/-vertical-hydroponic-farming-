package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "journals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // SALES, PURCHASE, CASH, BANK
}
