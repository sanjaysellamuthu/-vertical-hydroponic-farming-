package com.example.vertical_hydroponic_famming.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetReportDTO {
    private Long budgetId;
    private String budgetName;
    private String analyticAccountCode;
    private String analyticAccountName;
    
    private BigDecimal plannedRevenue;
    private BigDecimal actualRevenue;
    private BigDecimal revenueVariance;

    private BigDecimal plannedExpense;
    private BigDecimal actualExpense;
    private BigDecimal expenseVariance;

    private BigDecimal netPlannedResult;
    private BigDecimal netActualResult;
}
