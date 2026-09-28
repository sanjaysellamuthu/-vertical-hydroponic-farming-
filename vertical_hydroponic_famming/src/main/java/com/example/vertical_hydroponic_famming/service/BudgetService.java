package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.model.AnalyticAccount;
import com.example.vertical_hydroponic_famming.model.Budget;
import com.example.vertical_hydroponic_famming.repository.AnalyticAccountRepository;
import com.example.vertical_hydroponic_famming.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final AnalyticAccountRepository analyticAccountRepository;
    private final BudgetRepository budgetRepository;

    public AnalyticAccount createAnalyticAccount(AnalyticAccount account) {
        return analyticAccountRepository.save(account);
    }

    public List<AnalyticAccount> getAllAnalyticAccounts() {
        return analyticAccountRepository.findAll();
    }

    public Budget createBudget(Budget budget) {
        if (budget.getActualRevenue() == null) budget.setActualRevenue(BigDecimal.ZERO);
        if (budget.getActualExpense() == null) budget.setActualExpense(BigDecimal.ZERO);
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found with ID: " + id));
    }
}
