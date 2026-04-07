package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.AccountRepository;
import com.cajaviva.cajaviva.entity.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/person/{personId}")
    public List<Account> getAccountsByPerson(@PathVariable UUID personId) {
        return accountRepository.findByPersonId(personId);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }
}
