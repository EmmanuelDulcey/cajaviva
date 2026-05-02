package com.cajaviva.cajaviva.service;

import java.util.List;
import java.util.UUID;

import com.cajaviva.cajaviva.entity.Account;

public interface AccountService {

    List<Account> findAll();

    Account findById(UUID id);

    Account create(Account account);

    Account update(UUID id, Account account);

    void delete(UUID id);

    List<Account> findByUserId(UUID userId);

}
