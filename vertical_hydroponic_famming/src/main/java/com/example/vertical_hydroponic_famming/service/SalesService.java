package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.dto.OrderLineItemDTO;
import com.example.vertical_hydroponic_famming.dto.SalesOrderRequest;
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
public class SalesService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerInvoiceRepository invoiceRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final AccountingService accountingService;
    private final AccountRepository accountRepository;

    @Transactional
    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        Contact customer = contactRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + request.getCustomerId()));

        String soNum = "SO-" + System.currentTimeMillis();
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SalesOrderLine> lines = new ArrayList<>();

        SalesOrder so = SalesOrder.builder()
                .soNumber(soNum)
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status("CONFIRMED")
                .build();

        for (OrderLineItemDTO item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            SalesOrderLine line = SalesOrderLine.builder()
                    .salesOrder(so)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .subtotal(subtotal)
                    .build();
            lines.add(line);
        }

        so.setTotalAmount(totalAmount);
        so.setLines(lines);

        return salesOrderRepository.save(so);
    }

    @Transactional
    public CustomerInvoice convertSoToCustomerInvoice(Long soId) {
        SalesOrder so = salesOrderRepository.findById(soId)
                .orElseThrow(() -> new IllegalArgumentException("Sales Order not found: " + soId));

        if ("INVOICED".equalsIgnoreCase(so.getStatus())) {
            throw new IllegalStateException("Sales Order has already been invoiced");
        }

        String invNum = "INV-" + System.currentTimeMillis();

        CustomerInvoice invoice = CustomerInvoice.builder()
                .invoiceNumber(invNum)
                .salesOrderId(so.getId())
                .customer(so.getCustomer())
                .invoiceDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(15))
                .totalAmount(so.getTotalAmount())
                .status("UNPAID")
                .build();

        CustomerInvoice savedInvoice = invoiceRepository.save(invoice);

        so.setStatus("INVOICED");
        salesOrderRepository.save(so);

        // Auto-decrement available stock levels upon packaging and invoicing fulfilled produce
        for (SalesOrderLine line : so.getLines()) {
            Product prod = line.getProduct();
            double curStock = prod.getCurrentStock() != null ? prod.getCurrentStock() : 0.0;
            if (curStock < line.getQuantity()) {
                throw new IllegalStateException("Insufficient inventory stock for product: " + prod.getName() + ". Available: " + curStock + ", Requested: " + line.getQuantity());
            }
            prod.setCurrentStock(curStock - line.getQuantity());
            productRepository.save(prod);
        }

        // Generate Sales Journal Entry (Debit: Accounts Receivable / Cash 1030, Credit: Organic Produce Sales Revenue 4010)
        Journal salesJournal = accountingService.getJournalByCode("SALES");
        Account revenueAcc = accountingService.getAccountByCode("4010"); // Organic Produce Sales Revenue
        Account bankAcc = accountingService.getAccountByCode("1030"); // Cash / Bank Accounts Receivable

        JournalLine debitLine = JournalLine.builder()
                .account(bankAcc)
                .debit(so.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .description("Produce Sales Invoice #" + savedInvoice.getInvoiceNumber() + " - " + so.getCustomer().getName())
                .build();

        JournalLine creditLine = JournalLine.builder()
                .account(revenueAcc)
                .debit(BigDecimal.ZERO)
                .credit(so.getTotalAmount())
                .description("Produce Revenue")
                .build();

        accountingService.createAndPostJournalEntry(salesJournal, "Customer Invoice #" + savedInvoice.getInvoiceNumber(), List.of(debitLine, creditLine));

        return savedInvoice;
    }

    @Transactional
    public Payment collectCustomerPayment(Long invoiceId, Long bankAccountId) {
        CustomerInvoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));

        if ("PAID".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Invoice has already been paid");
        }

        Account bankAcc = accountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank Account not found: " + bankAccountId));

        Payment payment = Payment.builder()
                .paymentNumber("PAY-IN-" + System.currentTimeMillis())
                .paymentType("INBOUND")
                .contact(invoice.getCustomer())
                .bankAccount(bankAcc)
                .amount(invoice.getTotalAmount())
                .paymentDate(LocalDateTime.now())
                .reference("Payment for Invoice #" + invoice.getInvoiceNumber())
                .relatedInvoiceId(invoice.getId())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        // Generate Bank Journal Entry (Debit: Cash/Bank 1030, Credit: Accounts Receivable)
        Journal bankJournal = accountingService.getJournalByCode("BANK");
        Account revenueAcc = accountingService.getAccountByCode("4010");

        JournalLine debitLine = JournalLine.builder()
                .account(bankAcc)
                .debit(invoice.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .description("Customer Bank Transfer receipt - Invoice #" + invoice.getInvoiceNumber())
                .build();

        JournalLine creditLine = JournalLine.builder()
                .account(revenueAcc)
                .debit(BigDecimal.ZERO)
                .credit(invoice.getTotalAmount())
                .description("Settlement of Customer Invoice #" + invoice.getInvoiceNumber())
                .build();

        accountingService.createAndPostJournalEntry(bankJournal, "Inbound Payment #" + savedPayment.getPaymentNumber(), List.of(debitLine, creditLine));

        return savedPayment;
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public List<CustomerInvoice> getAllCustomerInvoices() {
        return invoiceRepository.findAll();
    }
}
