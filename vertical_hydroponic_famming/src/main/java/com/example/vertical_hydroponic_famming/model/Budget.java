package com.example.vertical_hydroponic_famming.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal plannedRevenue;

    private BigDecimal plannedExpense;

    private BigDecimal actualRevenue;

    private BigDecimal actualExpense;
}
