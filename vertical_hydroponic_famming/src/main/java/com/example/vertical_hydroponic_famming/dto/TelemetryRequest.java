package com.example.vertical_hydroponic_famming.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryRequest {

    @NotNull(message = "Rack ID is required")
    private Long rackId;

    private Double phLevel;

    private Double ecLevel;

    private Double temperatureCelsius;

    private Double humidityPercent;

    private Double lightLux;
}
