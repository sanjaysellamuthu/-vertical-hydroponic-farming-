package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.model.AnalyticAccount;
import com.example.vertical_hydroponic_famming.model.Budget;
import com.example.vertical_hydroponic_famming.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro")
@RequiredArgsConstructor
@Tag(name = "Analytic Accounts & Budgeting Controller", description = "Farm Module Analytic Accounts & Budget Operations")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/analytic-accounts")
    @Operation(summary = "Create Analytic Account for Farm Module (e.g., Vertical Module Zone 3)")
    public ResponseEntity<AnalyticAccount> createAnalyticAccount(@RequestBody AnalyticAccount account) {
        return new ResponseEntity<>(budgetService.createAnalyticAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/analytic-accounts")
    @Operation(summary = "Get all Analytic Accounts")
    public ResponseEntity<List<AnalyticAccount>> getAllAnalyticAccounts() {
        return ResponseEntity.ok(budgetService.getAllAnalyticAccounts());
    }

    @PostMapping("/budgets")
    @Operation(summary = "Create planned farm module budget vs actual expenditures & revenues")
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        return new ResponseEntity<>(budgetService.createBudget(budget), HttpStatus.CREATED);
    }

    @GetMapping("/budgets")
    @Operation(summary = "Get all Farm Budgets")
    public ResponseEntity<List<Budget>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }
}
