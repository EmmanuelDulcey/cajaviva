package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.service.AccountService;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Account findById(UUID id) {
        return repository.findById(id).orElse(null);
    }

@Override
public Account create(Account account) {
    if (account.getId() == null) {
        account.setId(java.util.UUID.randomUUID());
    }
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    account.setCreatedAt(now);
    account.setUpdatedAt(now);
    return repository.save(account);
}

@Override
public Account update(UUID id, Account account) {
    if (!repository.existsById(id)) {
        return null;
    }
    account.setId(id);
    account.setUpdatedAt(java.time.LocalDateTime.now());
    return repository.save(account);
}

@Override
public void delete(UUID id) {
    if (!repository.existsById(id)) {
        throw new com.cajaviva.cajaviva.exception.ResourceNotFoundException("Account not found: " + id);
    }
    repository.deleteById(id);
}

    @Override
    public List<Account> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }
}
