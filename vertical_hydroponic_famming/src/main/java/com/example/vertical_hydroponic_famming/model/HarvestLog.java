package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "harvest_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rackId;

    @Column(nullable = false)
    private Long productId;

    private Double yieldQuantityKg;

    private String qualityGrade; // GRADE_A, GRADE_B

    private LocalDateTime harvestDate;

    private String notes;
}
