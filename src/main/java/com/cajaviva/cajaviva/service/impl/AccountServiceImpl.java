package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.AccountDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.AccountService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

public AccountServiceImpl(@Qualifier("AccountJPAImpl") AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public Account findById(UUID id) {
        return accountDao.findById(id).orElse(null);
    }

    @Override
    public Account create(Account account) {
        return accountDao.save(account);
    }

    @Override
    public Account update(UUID id, Account account) {
        account.setId(id);
        return accountDao.save(account);
    }

    @Override
    public void delete(UUID id) {
        accountDao.deleteById(id);
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return accountDao.findByUserId(userId);
    }
}
