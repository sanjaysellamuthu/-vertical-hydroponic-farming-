package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.AnalyticAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyticAccountRepository extends JpaRepository<AnalyticAccount, Long> {
    Optional<AnalyticAccount> findByCode(String code);
}
