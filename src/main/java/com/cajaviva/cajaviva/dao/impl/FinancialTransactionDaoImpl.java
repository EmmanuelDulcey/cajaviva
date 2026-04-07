package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.FinancialTransactionDao;
import com.cajaviva.cajaviva.dao.jpa.FinancialTransactionJpaRepository;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialTransactionDaoImpl implements FinancialTransactionDao {

    private final FinancialTransactionJpaRepository financialTransactionJpaRepository;

    public FinancialTransactionDaoImpl(FinancialTransactionJpaRepository financialTransactionJpaRepository) {
        this.financialTransactionJpaRepository = financialTransactionJpaRepository;
    }

    @Override
    public List<FinancialTransaction> findAll() {
        return financialTransactionJpaRepository.findAll();
    }

    @Override
    public Optional<FinancialTransaction> findById(UUID id) {
        return financialTransactionJpaRepository.findById(id);
    }

    @Override
    public FinancialTransaction save(FinancialTransaction entity) {
        return financialTransactionJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return financialTransactionJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        financialTransactionJpaRepository.deleteById(id);
    }

    @Override
    public List<FinancialTransaction> findByAccountId(UUID account_id) {
        return financialTransactionJpaRepository.findByAccountId(account_id);
    }
}
