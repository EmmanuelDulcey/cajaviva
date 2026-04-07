package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.FinancialTransactionDao;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionDao financialTransactionDao;

    public FinancialTransactionController(FinancialTransactionDao financialTransactionDao) {
        this.financialTransactionDao = financialTransactionDao;
    }

    @GetMapping
    public List<FinancialTransaction> getAllTransactions() {
        return financialTransactionDao.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<FinancialTransaction> getTransactionsByAccount(@PathVariable("account_id") UUID account_id) {
        return financialTransactionDao.findByAccountId(account_id);
    }

    @PostMapping
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return financialTransactionDao.save(transaction);
    }
}
