package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.SensorTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorTelemetryRepository extends JpaRepository<SensorTelemetry, Long> {
    List<SensorTelemetry> findByRackIdOrderByRecordedAtDesc(Long rackId);
}
