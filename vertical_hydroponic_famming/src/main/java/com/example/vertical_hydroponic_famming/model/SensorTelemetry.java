package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rackId;

    private Double phLevel;

    private Double ecLevel;

    private Double temperatureCelsius;

    private Double humidityPercent;

    private Double lightLux;

    private LocalDateTime recordedAt;
}
