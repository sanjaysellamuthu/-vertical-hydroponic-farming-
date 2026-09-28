package com.example.vertical_hydroponic_famming.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderRequest {

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotEmpty(message = "Order lines cannot be empty")
    private List<OrderLineItemDTO> items;
}
