package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.dto.SalesOrderRequest;
import com.example.vertical_hydroponic_famming.model.CustomerInvoice;
import com.example.vertical_hydroponic_famming.model.Payment;
import com.example.vertical_hydroponic_famming.model.SalesOrder;
import com.example.vertical_hydroponic_famming.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro")
@RequiredArgsConstructor
@Tag(name = "Sales & Customer Invoicing Controller", description = "Wholesale Produce Orders, Customer Invoices & Bank Receipts")
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/sales-orders")
    @Operation(summary = "Issue wholesale produce sales order for butterhead lettuce")
    public ResponseEntity<SalesOrder> createSO(@Valid @RequestBody SalesOrderRequest request) {
        return new ResponseEntity<>(salesService.createSalesOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/sales-orders")
    @Operation(summary = "Get all Sales Orders")
    public ResponseEntity<List<SalesOrder>> getAllSOs() {
        return ResponseEntity.ok(salesService.getAllSalesOrders());
    }

    @PostMapping("/sales-orders/{id}/convert-to-invoice")
    @Operation(summary = "Convert fulfilled produce order to Customer Invoice & auto-decrement packaged stock")
    public ResponseEntity<CustomerInvoice> convertSoToInvoice(@PathVariable Long id) {
        return new ResponseEntity<>(salesService.convertSoToCustomerInvoice(id), HttpStatus.CREATED);
    }

    @GetMapping("/customer-invoices")
    @Operation(summary = "Get all Customer Invoices")
    public ResponseEntity<List<CustomerInvoice>> getAllInvoices() {
        return ResponseEntity.ok(salesService.getAllCustomerInvoices());
    }

    @PostMapping("/customer-invoices/{id}/pay")
    @Operation(summary = "Collect payment via Bank transfer and post Bank Journal entry")
    public ResponseEntity<Payment> collectPayment(@PathVariable Long id, @RequestParam Long bankAccountId) {
        return ResponseEntity.ok(salesService.collectCustomerPayment(id, bankAccountId));
    }
}
