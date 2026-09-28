package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.dto.HarvestRequest;
import com.example.vertical_hydroponic_famming.model.FarmRack;
import com.example.vertical_hydroponic_famming.model.HarvestLog;
import com.example.vertical_hydroponic_famming.model.Product;
import com.example.vertical_hydroponic_famming.repository.FarmRackRepository;
import com.example.vertical_hydroponic_famming.repository.HarvestLogRepository;
import com.example.vertical_hydroponic_famming.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final HarvestLogRepository harvestLogRepository;
    private final FarmRackRepository rackRepository;
    private final ProductRepository productRepository;

    @Transactional
    public HarvestLog logHarvest(HarvestRequest request) {
        FarmRack rack = rackRepository.findById(request.getRackId())
                .orElseThrow(() -> new IllegalArgumentException("Rack not found with ID: " + request.getRackId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

        HarvestLog harvest = HarvestLog.builder()
                .rackId(rack.getId())
                .productId(product.getId())
                .yieldQuantityKg(request.getYieldQuantityKg())
                .qualityGrade(request.getQualityGrade() != null ? request.getQualityGrade() : "GRADE_A")
                .harvestDate(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        harvestLogRepository.save(harvest);

        // Update product inventory stock
        double currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0.0;
        product.setCurrentStock(currentStock + request.getYieldQuantityKg());
        productRepository.save(product);

        // Update rack status to HARVESTED & clear crop growth cycle
        rack.setStatus("HARVESTED");
        rackRepository.save(rack);

        return harvest;
    }

    public List<HarvestLog> getAllHarvestLogs() {
        return harvestLogRepository.findAll();
    }

    public List<HarvestLog> getHarvestLogsByRack(Long rackId) {
        return harvestLogRepository.findByRackId(rackId);
    }
}
