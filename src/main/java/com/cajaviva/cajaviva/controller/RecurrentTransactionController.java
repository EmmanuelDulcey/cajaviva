package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.RecurrentTransactionDao;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recurrent-transactions")
public class RecurrentTransactionController {

    private final RecurrentTransactionDao recurrentTransactionDao;

    public RecurrentTransactionController(RecurrentTransactionDao recurrentTransactionDao) {
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    @GetMapping
    public List<RecurrentTransaction> getAllRecurrentTransactions() {
        return recurrentTransactionDao.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<RecurrentTransaction> getByAccount(@PathVariable("account_id") UUID account_id) {
        return recurrentTransactionDao.findByAccountId(account_id);
    }

    @PostMapping
    public RecurrentTransaction createRecurrentTransaction(@RequestBody RecurrentTransaction recurrentTransaction) {
        return recurrentTransactionDao.save(recurrentTransaction);
    }
}
