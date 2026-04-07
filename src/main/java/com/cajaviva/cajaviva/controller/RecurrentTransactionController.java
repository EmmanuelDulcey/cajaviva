package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.RecurrentTransactionRepository;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recurrent-transactions")
public class RecurrentTransactionController {

    private final RecurrentTransactionRepository recurrentTransactionRepository;

    public RecurrentTransactionController(RecurrentTransactionRepository recurrentTransactionRepository) {
        this.recurrentTransactionRepository = recurrentTransactionRepository;
    }

    @GetMapping
    public List<RecurrentTransaction> getAllRecurrentTransactions() {
        return recurrentTransactionRepository.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<RecurrentTransaction> getByAccount(@PathVariable UUID accountId) {
        return recurrentTransactionRepository.findByAccountId(accountId);
    }

    @PostMapping
    public RecurrentTransaction createRecurrentTransaction(@RequestBody RecurrentTransaction recurrentTransaction) {
        return recurrentTransactionRepository.save(recurrentTransaction);
    }
}
