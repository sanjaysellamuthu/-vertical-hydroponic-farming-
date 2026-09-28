package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.dto.PurchaseOrderRequest;
import com.example.vertical_hydroponic_famming.model.Payment;
import com.example.vertical_hydroponic_famming.model.PurchaseOrder;
import com.example.vertical_hydroponic_famming.model.VendorBill;
import com.example.vertical_hydroponic_famming.service.PurchaseService;
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
@Tag(name = "Procurement & Vendor Bills Controller", description = "Purchase Orders, Liquid Nutrient Bills & Vendor Bank Payments")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/purchase-orders")
    @Operation(summary = "Create Purchase Order (PO) for hydroponic liquid nutrient concentrates")
    public ResponseEntity<PurchaseOrder> createPO(@Valid @RequestBody PurchaseOrderRequest request) {
        return new ResponseEntity<>(purchaseService.createPurchaseOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/purchase-orders")
    @Operation(summary = "Get all Purchase Orders")
    public ResponseEntity<List<PurchaseOrder>> getAllPOs() {
        return ResponseEntity.ok(purchaseService.getAllPurchaseOrders());
    }

    @PostMapping("/purchase-orders/{id}/convert-to-bill")
    @Operation(summary = "Convert Purchase Order into Vendor Bill and post Purchase Journal entry")
    public ResponseEntity<VendorBill> convertPoToBill(@PathVariable Long id) {
        return new ResponseEntity<>(purchaseService.convertPoToVendorBill(id), HttpStatus.CREATED);
    }

    @GetMapping("/vendor-bills")
    @Operation(summary = "Get all Vendor Bills")
    public ResponseEntity<List<VendorBill>> getAllBills() {
        return ResponseEntity.ok(purchaseService.getAllVendorBills());
    }

    @PostMapping("/vendor-bills/{id}/pay")
    @Operation(summary = "Pay Vendor Bill via Bank transfer and post Bank Journal entry")
    public ResponseEntity<Payment> payBill(@PathVariable Long id, @RequestParam Long bankAccountId) {
        return ResponseEntity.ok(purchaseService.payVendorBill(id, bankAccountId));
    }
}
