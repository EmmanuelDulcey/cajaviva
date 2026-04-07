package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.FinancialTransactionRepository;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionRepository transactionRepository;

    public FinancialTransactionController(FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<FinancialTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<FinancialTransaction> getTransactionsByAccount(@PathVariable UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @PostMapping
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return transactionRepository.save(transaction);
    }
}
