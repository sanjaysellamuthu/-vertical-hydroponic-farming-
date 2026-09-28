package com.example.vertical_hydroponic_famming.service;

import com.example.vertical_hydroponic_famming.model.*;
import com.example.vertical_hydroponic_famming.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final AccountRepository accountRepository;
    private final JournalRepository journalRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalLineRepository journalLineRepository;

    public Account createAccount(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByCode(String code) {
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with code: " + code));
    }

    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Journal getJournalByCode(String code) {
        return journalRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Journal not found with code: " + code));
    }

    @Transactional
    public JournalEntry createAndPostJournalEntry(Journal journal, String reference, List<JournalLine> lines) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (JournalLine line : lines) {
            if (line.getDebit() != null) totalDebit = totalDebit.add(line.getDebit());
            if (line.getCredit() != null) totalCredit = totalCredit.add(line.getCredit());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalArgumentException("Journal Entry is unbalanced! Total Debit: " + totalDebit + ", Total Credit: " + totalCredit);
        }

        String entryNum = "JE-" + System.currentTimeMillis();

        JournalEntry entry = JournalEntry.builder()
                .entryNumber(entryNum)
                .entryDate(LocalDateTime.now())
                .journal(journal)
                .reference(reference)
                .status("POSTED")
                .build();

        JournalEntry savedEntry = journalEntryRepository.save(entry);

        for (JournalLine line : lines) {
            line.setJournalEntry(savedEntry);
            journalLineRepository.save(line);

            // Update Account Balance
            Account account = line.getAccount();
            BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;

            if ("ASSET".equalsIgnoreCase(account.getType()) || "EXPENSE".equalsIgnoreCase(account.getType())) {
                BigDecimal debit = line.getDebit() != null ? line.getDebit() : BigDecimal.ZERO;
                BigDecimal credit = line.getCredit() != null ? line.getCredit() : BigDecimal.ZERO;
                account.setBalance(currentBalance.add(debit).subtract(credit));
            } else { // LIABILITY, INCOME, EQUITY
                BigDecimal debit = line.getDebit() != null ? line.getDebit() : BigDecimal.ZERO;
                BigDecimal credit = line.getCredit() != null ? line.getCredit() : BigDecimal.ZERO;
                account.setBalance(currentBalance.add(credit).subtract(debit));
            }
            accountRepository.save(account);
        }

        savedEntry.setLines(lines);
        return savedEntry;
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }
}
