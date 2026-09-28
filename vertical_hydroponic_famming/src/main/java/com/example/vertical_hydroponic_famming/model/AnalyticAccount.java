package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analytic_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;
}
