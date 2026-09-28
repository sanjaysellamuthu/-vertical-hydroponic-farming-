package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "farm_racks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmRack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rackCode;

    @Column(nullable = false)
    private String name;

    private String zone;

    private String status; // ACTIVE, MAINTENANCE, HARVESTED

    private String currentCrop;

    private Double targetPh;

    private Double targetEc;

    private Double currentPh;

    private Double currentEc;

    private Integer lightDurationHours;

    private Integer lightIntensityPercent;

    private Boolean lightActive;
}
