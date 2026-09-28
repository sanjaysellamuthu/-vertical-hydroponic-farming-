package com.example.vertical_hydroponic_famming.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Payment type is required (INBOUND or OUTBOUND)")
    private String paymentType;

    @NotNull(message = "Contact ID is required")
    private Long contactId;

    @NotNull(message = "Bank Account ID is required")
    private Long bankAccountId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String reference;

    private Long relatedInvoiceId;

    private Long relatedBillId;
}
