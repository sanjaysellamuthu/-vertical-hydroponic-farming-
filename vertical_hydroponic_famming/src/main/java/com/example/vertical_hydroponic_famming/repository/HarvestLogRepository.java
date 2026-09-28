package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.HarvestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestLogRepository extends JpaRepository<HarvestLog, Long> {
    List<HarvestLog> findByRackId(Long rackId);
}
