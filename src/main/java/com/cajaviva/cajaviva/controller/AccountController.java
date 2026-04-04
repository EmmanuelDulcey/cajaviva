package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.save(account);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable UUID id) {
        return accountService.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteById(id);
    }
}
