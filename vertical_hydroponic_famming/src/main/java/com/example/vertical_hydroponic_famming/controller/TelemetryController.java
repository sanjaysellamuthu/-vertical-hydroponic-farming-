package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.dto.TelemetryRequest;
import com.example.vertical_hydroponic_famming.model.DosingLog;
import com.example.vertical_hydroponic_famming.model.SensorTelemetry;
import com.example.vertical_hydroponic_famming.service.FarmRackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro")
@RequiredArgsConstructor
@Tag(name = "Telemetry & Actuators Controller", description = "Sensor Telemetry Ingestion & Dosing Pump Control")
public class TelemetryController {

    private final FarmRackService rackService;

    @PostMapping("/telemetry")
    @Operation(summary = "Ingest sensor telemetry (pH/EC, Temp, Humidity, Lux) and auto-trigger dosing")
    public ResponseEntity<SensorTelemetry> recordTelemetry(@Valid @RequestBody TelemetryRequest request) {
        return new ResponseEntity<>(rackService.recordTelemetry(request), HttpStatus.CREATED);
    }

    @GetMapping("/telemetry/rack/{rackId}")
    @Operation(summary = "Get historical telemetry records for a specific farm rack")
    public ResponseEntity<List<SensorTelemetry>> getTelemetryByRack(@PathVariable Long rackId) {
        return ResponseEntity.ok(rackService.getTelemetryForRack(rackId));
    }

    @GetMapping("/dosing/rack/{rackId}")
    @Operation(summary = "Get automated dosing pump execution logs for a rack")
    public ResponseEntity<List<DosingLog>> getDosingLogsByRack(@PathVariable Long rackId) {
        return ResponseEntity.ok(rackService.getDosingLogsForRack(rackId));
    }
}
