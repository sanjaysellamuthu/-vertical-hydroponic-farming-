package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.dto.BalanceSheetDTO;
import com.example.vertical_hydroponic_famming.dto.BudgetReportDTO;
import com.example.vertical_hydroponic_famming.dto.ProfitAndLossDTO;
import com.example.vertical_hydroponic_famming.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hydro/reports")
@RequiredArgsConstructor
@Tag(name = "Financial & Operational Reports Controller", description = "Balance Sheet, Profit & Loss Account, and Operational Budget Reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/balance-sheet")
    @Operation(summary = "Generate Balance Sheet: Farm rack assets and inventory value vs open utility liabilities")
    public ResponseEntity<BalanceSheetDTO> getBalanceSheet() {
        return ResponseEntity.ok(reportService.generateBalanceSheet());
    }

    @GetMapping("/profit-loss")
    @Operation(summary = "Generate Profit & Loss Account: Produce revenue minus electricity, seed, and nutrient costs")
    public ResponseEntity<ProfitAndLossDTO> getProfitAndLoss() {
        return ResponseEntity.ok(reportService.generateProfitAndLoss());
    }

    @GetMapping("/budget-report/{budgetId}")
    @Operation(summary = "Generate Hydroponic Farm Operational Budget Report (Planned vs Actual Variances)")
    public ResponseEntity<BudgetReportDTO> getBudgetReport(@PathVariable Long budgetId) {
        return ResponseEntity.ok(reportService.generateBudgetReport(budgetId));
    }
}
