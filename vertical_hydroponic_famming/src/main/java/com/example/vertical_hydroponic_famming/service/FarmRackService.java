package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.dto.TelemetryRequest;
import com.example.vertical_hydroponic_famming.model.DosingLog;
import com.example.vertical_hydroponic_famming.model.FarmRack;
import com.example.vertical_hydroponic_famming.model.SensorTelemetry;
import com.example.vertical_hydroponic_famming.repository.DosingLogRepository;
import com.example.vertical_hydroponic_famming.repository.FarmRackRepository;
import com.example.vertical_hydroponic_famming.repository.SensorTelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmRackService {

    private final FarmRackRepository rackRepository;
    private final SensorTelemetryRepository telemetryRepository;
    private final DosingLogRepository dosingLogRepository;

    public FarmRack createRack(FarmRack rack) {
        if (rack.getStatus() == null) {
            rack.setStatus("ACTIVE");
        }
        if (rack.getLightActive() == null) {
            rack.setLightActive(true);
        }
        return rackRepository.save(rack);
    }

    public List<FarmRack> getAllRacks() {
        return rackRepository.findAll();
    }

    public FarmRack getRackById(Long id) {
        return rackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Farm rack not found with ID: " + id));
    }

    public FarmRack updateLightingParameters(Long id, Integer durationHours, Integer intensityPercent, Boolean active) {
        FarmRack rack = getRackById(id);
        if (durationHours != null) rack.setLightDurationHours(durationHours);
        if (intensityPercent != null) rack.setLightIntensityPercent(intensityPercent);
        if (active != null) rack.setLightActive(active);
        return rackRepository.save(rack);
    }

    @Transactional
    public SensorTelemetry recordTelemetry(TelemetryRequest request) {
        FarmRack rack = getRackById(request.getRackId());

        SensorTelemetry telemetry = SensorTelemetry.builder()
                .rackId(rack.getId())
                .phLevel(request.getPhLevel())
                .ecLevel(request.getEcLevel())
                .temperatureCelsius(request.getTemperatureCelsius())
                .humidityPercent(request.getHumidityPercent())
                .lightLux(request.getLightLux())
                .recordedAt(LocalDateTime.now())
                .build();

        telemetryRepository.save(telemetry);

        // Update current rack metrics
        if (request.getPhLevel() != null) rack.setCurrentPh(request.getPhLevel());
        if (request.getEcLevel() != null) rack.setCurrentEc(request.getEcLevel());
        rackRepository.save(rack);

        // Auto-dosing check: compare current metrics with target metrics
        checkAndExecuteAutoDosing(rack, request.getPhLevel(), request.getEcLevel());

        return telemetry;
    }

    private void checkAndExecuteAutoDosing(FarmRack rack, Double ph, Double ec) {
        double nutrientMl = 0.0;
        double phAdjusterMl = 0.0;

        if (ec != null && rack.getTargetEc() != null && ec < rack.getTargetEc() - 0.2) {
            nutrientMl = (rack.getTargetEc() - ec) * 100.0; // 100 ml per 0.1 EC deficit
        }

        if (ph != null && rack.getTargetPh() != null && Math.abs(ph - rack.getTargetPh()) > 0.3) {
            phAdjusterMl = Math.abs(ph - rack.getTargetPh()) * 50.0;
        }

        if (nutrientMl > 0 || phAdjusterMl > 0) {
            DosingLog dosing = DosingLog.builder()
                    .rackId(rack.getId())
                    .nutrientAmountMl(nutrientMl)
                    .phAdjusterAmountMl(phAdjusterMl)
                    .status("AUTO_DOSING_EXECUTED")
                    .timestamp(LocalDateTime.now())
                    .build();
            dosingLogRepository.save(dosing);
        }
    }

    public List<SensorTelemetry> getTelemetryForRack(Long rackId) {
        return telemetryRepository.findByRackIdOrderByRecordedAtDesc(rackId);
    }

    public List<DosingLog> getDosingLogsForRack(Long rackId) {
        return dosingLogRepository.findByRackIdOrderByTimestampDesc(rackId);
    }
}
