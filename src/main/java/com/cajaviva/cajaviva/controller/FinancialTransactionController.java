package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.FinancialTransactionService;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    public FinancialTransactionController(FinancialTransactionService financialTransactionService) {
        this.financialTransactionService = financialTransactionService;
    }

    @GetMapping
    public List<FinancialTransaction> getAllTransactions() {
        return financialTransactionService.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<FinancialTransaction> getTransactionsByAccount(@PathVariable("account_id") UUID account_id) {
        return financialTransactionService.findByAccountId(account_id);
    }

    @PostMapping
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return financialTransactionService.create(transaction);
    }
}
