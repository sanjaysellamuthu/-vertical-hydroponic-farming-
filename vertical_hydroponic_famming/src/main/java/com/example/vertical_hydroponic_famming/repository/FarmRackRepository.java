package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.FarmRack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmRackRepository extends JpaRepository<FarmRack, Long> {
    Optional<FarmRack> findByRackCode(String rackCode);
}
