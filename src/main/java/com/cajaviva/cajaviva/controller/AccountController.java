package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.AccountDao;
import com.cajaviva.cajaviva.entity.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    @GetMapping("/user/{user_id}")
    public List<Account> getAccountsByUser(@PathVariable("user_id") UUID user_id) {
        return accountDao.findByUserId(user_id);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountDao.save(account);
    }
}
