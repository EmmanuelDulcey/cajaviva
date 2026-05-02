package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.RecurrentTransactionService;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recurrent-transactions")
public class RecurrentTransactionController {

    private final RecurrentTransactionService recurrentTransactionService;

    public RecurrentTransactionController(RecurrentTransactionService recurrentTransactionService) {
        this.recurrentTransactionService = recurrentTransactionService;
    }

    @GetMapping
    public List<RecurrentTransaction> getAllRecurrentTransactions() {
        return recurrentTransactionService.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<RecurrentTransaction> getByAccount(@PathVariable("account_id") UUID account_id) {
        return recurrentTransactionService.findByAccountId(account_id);
    }

    @PostMapping
    public RecurrentTransaction createRecurrentTransaction(@RequestBody RecurrentTransaction recurrentTransaction) {
        return recurrentTransactionService.create(recurrentTransaction);
    }
}
