package com.example.vertical_hydroponic_famming.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceSheetDTO {
    private BigDecimal verticalRackAssets;
    private BigDecimal produceStockValue;
    private BigDecimal cashAndBankBalance;
    private BigDecimal totalAssets;

    private BigDecimal nutrientSupplierPayables;
    private BigDecimal utilityCreditors;
    private BigDecimal totalLiabilities;

    private BigDecimal netEquity;
}
