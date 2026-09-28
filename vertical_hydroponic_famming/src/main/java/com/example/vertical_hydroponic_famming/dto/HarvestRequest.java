package com.example.vertical_hydroponic_famming.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestRequest {

    @NotNull(message = "Rack ID is required")
    private Long rackId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Yield quantity in kg is required")
    private Double yieldQuantityKg;

    private String qualityGrade; // GRADE_A, GRADE_B

    private String notes;
}
