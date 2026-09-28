package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dosing_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DosingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rackId;

    private Double nutrientAmountMl;

    private Double phAdjusterAmountMl;

    private String status; // AUTO_DOSING_EXECUTED, MANUAL

    private LocalDateTime timestamp;
}
