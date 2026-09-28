package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.dto.HarvestRequest;
import com.example.vertical_hydroponic_famming.model.HarvestLog;
import com.example.vertical_hydroponic_famming.service.HarvestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro/harvest")
@RequiredArgsConstructor
@Tag(name = "Harvest & Crop Yield Controller", description = "Record Produce Harvest & Increment Available Inventory Stock")
public class HarvestController {

    private final HarvestService harvestService;

    @PostMapping
    @Operation(summary = "Log produce harvest yield and auto-increment available inventory stock")
    public ResponseEntity<HarvestLog> logHarvest(@Valid @RequestBody HarvestRequest request) {
        return new ResponseEntity<>(harvestService.logHarvest(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all harvest logs")
    public ResponseEntity<List<HarvestLog>> getAllHarvests() {
        return ResponseEntity.ok(harvestService.getAllHarvestLogs());
    }

    @GetMapping("/rack/{rackId}")
    @Operation(summary = "Get harvest logs for a specific farm rack")
    public ResponseEntity<List<HarvestLog>> getHarvestsByRack(@PathVariable Long rackId) {
        return ResponseEntity.ok(harvestService.getHarvestLogsByRack(rackId));
    }
}
