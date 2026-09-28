package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByAnalyticAccountId(Long analyticAccountId);
}
