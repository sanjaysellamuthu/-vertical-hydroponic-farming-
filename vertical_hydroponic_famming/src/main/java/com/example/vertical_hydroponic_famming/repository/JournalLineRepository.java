package com.example.vertical_hydroponic_famming.repository;

import com.example.vertical_hydroponic_famming.model.JournalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalLineRepository extends JpaRepository<JournalLine, Long> {
    List<JournalLine> findByAccountId(Long accountId);
    List<JournalLine> findByAnalyticAccountId(Long analyticAccountId);
}
