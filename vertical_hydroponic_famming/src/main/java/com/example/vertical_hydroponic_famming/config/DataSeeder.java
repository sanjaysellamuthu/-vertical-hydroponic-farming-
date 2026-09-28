package com.example.vertical_hydroponic_famming.config;

import com.example.vertical_hydroponic_famming.model.*;
import com.example.vertical_hydroponic_famming.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final JournalRepository journalRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final FarmRackRepository rackRepository;
    private final AnalyticAccountRepository analyticAccountRepository;
    private final BudgetRepository budgetRepository;
    private final SensorTelemetryRepository telemetryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() > 0) {
            return; // Data already seeded
        }

        // 1. Chart of Accounts Master
        Account rackAssets = accountRepository.save(Account.builder().code("1010").name("Vertical Rack Assets").type("ASSET").balance(new BigDecimal("25000.00")).build());
        Account produceStock = accountRepository.save(Account.builder().code("1020").name("Produce Stock").type("ASSET").balance(new BigDecimal("3500.00")).build());
        Account bank = accountRepository.save(Account.builder().code("1030").name("Cash/Bank").type("ASSET").balance(new BigDecimal("15000.00")).build());

        Account supplierPayables = accountRepository.save(Account.builder().code("2010").name("Nutrient Supplier Payables").type("LIABILITY").balance(BigDecimal.ZERO).build());
        Account utilityCreditors = accountRepository.save(Account.builder().code("2020").name("Utility Creditors").type("LIABILITY").balance(new BigDecimal("450.00")).build());

        Account salesRevenue = accountRepository.save(Account.builder().code("4010").name("Organic Produce Sales Revenue").type("INCOME").balance(BigDecimal.ZERO).build());

        Account elecExpenses = accountRepository.save(Account.builder().code("5010").name("Electricity Utility Expenses").type("EXPENSE").balance(new BigDecimal("450.00")).build());
        Account nutrientExpenses = accountRepository.save(Account.builder().code("5020").name("Nutrient Solution Expenditures").type("EXPENSE").balance(new BigDecimal("800.00")).build());

        // 2. Journal Master
        journalRepository.save(Journal.builder().code("SALES").name("Sales Journal").type("SALES").build());
        journalRepository.save(Journal.builder().code("PURCHASE").name("Purchase Journal").type("PURCHASE").build());
        journalRepository.save(Journal.builder().code("CASH").name("Cash Journal").type("CASH").build());
        journalRepository.save(Journal.builder().code("BANK").name("Bank Journal").type("BANK").build());

        // 3. Contact Master
        Contact grocer = contactRepository.save(Contact.builder()
                .code("CUST-001")
                .name("FreshMart Wholesale Grocers")
                .type("CUSTOMER")
                .email("procurement@freshmart.com")
                .phone("+1-555-0199")
                .address("100 Market St, City Center")
                .build());

        Contact vendor = contactRepository.save(Contact.builder()
                .code("VEND-001")
                .name("HydroTech Liquid Solutions Ltd")
                .type("VENDOR")
                .email("orders@hydrotech.com")
                .phone("+1-555-0822")
                .address("45 AgriPark Industrial Estate")
                .build());

        // 4. Product Master
        Product lettuce = productRepository.save(Product.builder()
                .code("PROD-001")
                .name("Organic Hydroponic Butterhead Lettuce")
                .type("PRODUCE")
                .unitOfMeasure("KG")
                .unitPrice(new BigDecimal("12.50"))
                .costPrice(new BigDecimal("4.00"))
                .currentStock(150.0) // 150 kg harvested ready stock
                .build());

        Product nutrient = productRepository.save(Product.builder()
                .code("SUP-001")
                .name("Hydroponic Nutrient Solution Concentrate A/B")
                .type("SUPPLY")
                .unitOfMeasure("LITRE")
                .unitPrice(new BigDecimal("25.00"))
                .costPrice(new BigDecimal("18.00"))
                .currentStock(50.0) // 50 Litres in stock
                .build());

        productRepository.save(Product.builder()
                .code("EQ-001")
                .name("LED Spectrum Growth Lighting Fixture 200W")
                .type("EQUIPMENT")
                .unitOfMeasure("UNIT")
                .unitPrice(new BigDecimal("220.00"))
                .costPrice(new BigDecimal("180.00"))
                .currentStock(12.0)
                .build());

        // 5. Farm Racks Master
        FarmRack rack1 = rackRepository.save(FarmRack.builder()
                .rackCode("RACK-ALPHA-1")
                .name("Vertical Module Zone 3 - Rack Alpha")
                .zone("Zone 3")
                .status("ACTIVE")
                .currentCrop("Organic Hydroponic Butterhead Lettuce")
                .targetPh(5.8)
                .targetEc(1.8)
                .currentPh(5.75)
                .currentEc(1.82)
                .lightDurationHours(16)
                .lightIntensityPercent(85)
                .lightActive(true)
                .build());

        rackRepository.save(FarmRack.builder()
                .rackCode("RACK-BETA-1")
                .name("Vertical Module Zone 1 - Rack Beta")
                .zone("Zone 1")
                .status("ACTIVE")
                .currentCrop("Hydroponic Romaine Lettuce")
                .targetPh(6.0)
                .targetEc(2.0)
                .currentPh(6.1)
                .currentEc(1.95)
                .lightDurationHours(14)
                .lightIntensityPercent(80)
                .lightActive(true)
                .build());

        // Initial Telemetry Record
        telemetryRepository.save(SensorTelemetry.builder()
                .rackId(rack1.getId())
                .phLevel(5.75)
                .ecLevel(1.82)
                .temperatureCelsius(22.5)
                .humidityPercent(65.0)
                .lightLux(12500.0)
                .recordedAt(LocalDateTime.now())
                .build());

        // 6. Analytic Accounts & Budget Master
        AnalyticAccount zone3Account = analyticAccountRepository.save(AnalyticAccount.builder()
                .code("ZONE-3")
                .name("Vertical Module Zone 3")
                .description("Analytic account tracking operational costs and produce revenue for Zone 3")
                .build());

        budgetRepository.save(Budget.builder()
                .name("Q3 Farm Module Zone 3 Operational Budget")
                .analyticAccount(zone3Account)
                .startDate(LocalDateTime.now().minusDays(30))
                .endDate(LocalDateTime.now().plusDays(60))
                .plannedRevenue(new BigDecimal("15000.00"))
                .plannedExpense(new BigDecimal("4500.00"))
                .actualRevenue(new BigDecimal("1875.00"))
                .actualExpense(new BigDecimal("1250.00"))
                .build());

        System.out.println(">>> Community Vertical Hydroponic Farming Master Data & Seeders Successfully Initialized! <<<");
    }
}
