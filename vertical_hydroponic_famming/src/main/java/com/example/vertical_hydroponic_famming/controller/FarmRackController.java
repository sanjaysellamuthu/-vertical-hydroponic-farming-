package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.model.FarmRack;
import com.example.vertical_hydroponic_famming.service.FarmRackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro/racks")
@RequiredArgsConstructor
@Tag(name = "Farm Rack Controller", description = "Vertical Hydroponic Rack Management & Environmental Parameters")
public class FarmRackController {

    private final FarmRackService rackService;

    @PostMapping
    @Operation(summary = "Create or Register a new Farm Rack")
    public ResponseEntity<FarmRack> createRack(@RequestBody FarmRack rack) {
        return new ResponseEntity<>(rackService.createRack(rack), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all vertical farm racks")
    public ResponseEntity<List<FarmRack>> getAllRacks() {
        return ResponseEntity.ok(rackService.getAllRacks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get farm rack details by ID")
    public ResponseEntity<FarmRack> getRackById(@PathVariable Long id) {
        return ResponseEntity.ok(rackService.getRackById(id));
    }

    @PutMapping("/{id}/lighting")
    @Operation(summary = "Update LED Growth Lighting duration, intensity, and on/off status")
    public ResponseEntity<FarmRack> updateLighting(
            @PathVariable Long id,
            @RequestParam(required = false) Integer durationHours,
            @RequestParam(required = false) Integer intensityPercent,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(rackService.updateLightingParameters(id, durationHours, intensityPercent, active));
    }
}
