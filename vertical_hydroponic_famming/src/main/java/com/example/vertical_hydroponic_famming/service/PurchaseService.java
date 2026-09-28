package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.dto.OrderLineItemDTO;
import com.example.vertical_hydroponic_famming.dto.PurchaseOrderRequest;
import com.example.vertical_hydroponic_famming.model.*;
import com.example.vertical_hydroponic_famming.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseOrderRepository poRepository;
    private final VendorBillRepository billRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final AccountingService accountingService;
    private final AccountRepository accountRepository;

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        Contact vendor = contactRepository.findById(request.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + request.getVendorId()));

        String poNum = "PO-" + System.currentTimeMillis();
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseOrderLine> lines = new ArrayList<>();

        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNum)
                .vendor(vendor)
                .orderDate(LocalDateTime.now())
                .status("CONFIRMED")
                .build();

        for (OrderLineItemDTO item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            PurchaseOrderLine line = PurchaseOrderLine.builder()
                    .purchaseOrder(po)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .subtotal(subtotal)
                    .build();
            lines.add(line);
        }

        po.setTotalAmount(totalAmount);
        po.setLines(lines);

        return poRepository.save(po);
    }

    @Transactional
    public VendorBill convertPoToVendorBill(Long poId) {
        PurchaseOrder po = poRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("PO not found with ID: " + poId));

        if ("BILLED".equalsIgnoreCase(po.getStatus())) {
            throw new IllegalStateException("PO has already been converted to Vendor Bill");
        }

        String billNum = "BILL-" + System.currentTimeMillis();

        VendorBill bill = VendorBill.builder()
                .billNumber(billNum)
                .purchaseOrderId(po.getId())
                .vendor(po.getVendor())
                .billDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(30))
                .totalAmount(po.getTotalAmount())
                .status("UNPAID")
                .build();

        VendorBill savedBill = billRepository.save(bill);

        // Update PO status
        po.setStatus("BILLED");
        poRepository.save(po);

        // Update product inventory for nutrient concentrates / supplies
        for (PurchaseOrderLine line : po.getLines()) {
            Product prod = line.getProduct();
            double curStock = prod.getCurrentStock() != null ? prod.getCurrentStock() : 0.0;
            prod.setCurrentStock(curStock + line.getQuantity());
            productRepository.save(prod);
        }

        // Generate Purchase Journal Entry (Debit: Nutrient Solution Expenditures 5020, Credit: Nutrient Supplier Payables 2010)
        Journal purchaseJournal = accountingService.getJournalByCode("PURCHASE");
        Account expenseAcc = accountingService.getAccountByCode("5020"); // Nutrient Solution Expenditures
        Account payableAcc = accountingService.getAccountByCode("2010"); // Nutrient Supplier Payables

        JournalLine debitLine = JournalLine.builder()
                .account(expenseAcc)
                .debit(po.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .description("Nutrient supply purchase - Bill #" + savedBill.getBillNumber())
                .build();

        JournalLine creditLine = JournalLine.builder()
                .account(payableAcc)
                .debit(BigDecimal.ZERO)
                .credit(po.getTotalAmount())
                .description("Vendor payable - " + po.getVendor().getName())
                .build();

        accountingService.createAndPostJournalEntry(purchaseJournal, "Vendor Bill #" + savedBill.getBillNumber(), List.of(debitLine, creditLine));

        return savedBill;
    }

    @Transactional
    public Payment payVendorBill(Long billId, Long bankAccountId) {
        VendorBill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor Bill not found: " + billId));

        if ("PAID".equalsIgnoreCase(bill.getStatus())) {
            throw new IllegalStateException("Bill has already been paid");
        }

        Account bankAcc = accountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank Account not found: " + bankAccountId));

        Payment payment = Payment.builder()
                .paymentNumber("PAY-OUT-" + System.currentTimeMillis())
                .paymentType("OUTBOUND")
                .contact(bill.getVendor())
                .bankAccount(bankAcc)
                .amount(bill.getTotalAmount())
                .paymentDate(LocalDateTime.now())
                .reference("Payment for Bill #" + bill.getBillNumber())
                .relatedBillId(bill.getId())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        bill.setStatus("PAID");
        billRepository.save(bill);

        // Generate Bank Journal Entry (Debit: Vendor Supplier Payables 2010, Credit: Cash/Bank 1030)
        Journal bankJournal = accountingService.getJournalByCode("BANK");
        Account payableAcc = accountingService.getAccountByCode("2010");

        JournalLine debitLine = JournalLine.builder()
                .account(payableAcc)
                .debit(bill.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .description("Settle vendor payable - " + bill.getVendor().getName())
                .build();

        JournalLine creditLine = JournalLine.builder()
                .account(bankAcc)
                .debit(BigDecimal.ZERO)
                .credit(bill.getTotalAmount())
                .description("Bank payout for Bill #" + bill.getBillNumber())
                .build();

        accountingService.createAndPostJournalEntry(bankJournal, "Outbound Payment #" + savedPayment.getPaymentNumber(), List.of(debitLine, creditLine));

        return savedPayment;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return poRepository.findAll();
    }

    public List<VendorBill> getAllVendorBills() {
        return billRepository.findAll();
    }
}
