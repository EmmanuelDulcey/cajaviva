package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.service.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionService transactionService;

    @GetMapping
    public List<FinancialTransaction> getAllTransactions() {
        return transactionService.findAll();
    }

    @PostMapping
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return transactionService.save(transaction);
    }

    @GetMapping("/{id}")
    public FinancialTransaction getTransactionById(@PathVariable UUID id) {
        return transactionService.findById(id).orElse(null);
    }
}