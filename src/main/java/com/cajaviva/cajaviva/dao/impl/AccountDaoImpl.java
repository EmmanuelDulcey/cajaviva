package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.AccountDao;
import com.cajaviva.cajaviva.dao.jpa.AccountJpaRepository;
import com.cajaviva.cajaviva.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final AccountJpaRepository accountJpaRepository;

    public AccountDaoImpl(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountJpaRepository.findAll();
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountJpaRepository.findById(id);
    }

    @Override
    public Account save(Account entity) {
        return accountJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return accountJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        accountJpaRepository.deleteById(id);
    }

    @Override
    public List<Account> findByUserId(UUID user_id) {
        return accountJpaRepository.findByUserId(user_id);
    }
}
