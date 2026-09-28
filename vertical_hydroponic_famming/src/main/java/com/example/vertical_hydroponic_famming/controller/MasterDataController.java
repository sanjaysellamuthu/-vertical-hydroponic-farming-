package com.example.vertical_hydroponic_famming.controller;

import com.example.vertical_hydroponic_famming.model.*;
import com.example.vertical_hydroponic_famming.repository.ContactRepository;
import com.example.vertical_hydroponic_famming.repository.ProductRepository;
import com.example.vertical_hydroponic_famming.service.AccountingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hydro")
@RequiredArgsConstructor
@Tag(name = "Master Data Modules", description = "Contact Master, Product Master, Chart of Accounts & Journals")
public class MasterDataController {

    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final AccountingService accountingService;

    // Contact Master
    @PostMapping("/contacts")
    @Operation(summary = "Register Wholesale Grocer Customer or Hydroponic Vendor")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.CREATED);
    }

    @GetMapping("/contacts")
    @Operation(summary = "Get all registered contacts")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactRepository.findAll());
    }

    // Product Master
    @PostMapping("/products")
    @Operation(summary = "Add Product (Organic Produce Goods, Nutrient Supplies, LED Fixtures)")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getCurrentStock() == null) product.setCurrentStock(0.0);
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @Operation(summary = "Get all master products and stock levels")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // Chart of Accounts Master
    @PostMapping("/accounts")
    @Operation(summary = "Create Chart of Accounts entry (Asset, Liability, Income, Expense)")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountingService.createAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    @Operation(summary = "Get Chart of Accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountingService.getAllAccounts());
    }

    // Journal Master
    @PostMapping("/journals")
    @Operation(summary = "Create Accounting Journal (Sales, Purchase, Cash, Bank)")
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        return new ResponseEntity<>(accountingService.createJournal(journal), HttpStatus.CREATED);
    }

    @GetMapping("/journals")
    @Operation(summary = "Get Accounting Journals")
    public ResponseEntity<List<Journal>> getAllJournals() {
        return ResponseEntity.ok(accountingService.getAllJournals());
    }
}
