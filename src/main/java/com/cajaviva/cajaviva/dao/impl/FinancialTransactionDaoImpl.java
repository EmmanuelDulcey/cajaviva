package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.FinancialTransactionDao;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialTransactionDaoImpl implements FinancialTransactionDao {

    private final FinancialTransactionRepository transactionRepository;

    public FinancialTransactionDaoImpl(FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<FinancialTransaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<FinancialTransaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    @Override
    public FinancialTransaction save(FinancialTransaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return transactionRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<FinancialTransaction> findByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public List<FinancialTransaction> findByCategory(Category category) {
        return transactionRepository.findByCategory(category);
    }

    @Override
    public List<FinancialTransaction> findByStatus(Integer status) {
        return transactionRepository.findByStatus(status);
    }

    @Override
    public List<FinancialTransaction> findByAccountId(UUID account_id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByAccountId'");
    }

    @Override
    public List<FinancialTransaction> findByCategoryId(UUID categoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'findByCategoryId'");
    }

    @Override
    public List<FinancialTransaction> findByAccountAndCategory(Account account, Category category) {
        throw new UnsupportedOperationException("Unimplemented method 'findByAccountAndCategory'");
    }
}
