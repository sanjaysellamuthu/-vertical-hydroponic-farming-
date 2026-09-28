package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.DosingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DosingLogRepository extends JpaRepository<DosingLog, Long> {
    List<DosingLog> findByRackIdOrderByTimestampDesc(Long rackId);
}
