package com.example.vertical_hydroponic_famming.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
}
