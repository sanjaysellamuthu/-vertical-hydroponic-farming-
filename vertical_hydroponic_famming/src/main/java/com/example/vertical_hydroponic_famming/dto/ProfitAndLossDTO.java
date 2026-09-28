package com.example.vertical_hydroponic_famming.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitAndLossDTO {
    private BigDecimal organicProduceSalesRevenue;
    private BigDecimal electricityUtilityExpenses;
    private BigDecimal nutrientSolutionExpenditures;
    private BigDecimal totalExpenses;
    private BigDecimal netProfitOrLoss;
}
