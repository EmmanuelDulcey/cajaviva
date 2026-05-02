package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.service.FinancialTransactionService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;

    public FinancialTransactionServiceImpl(@Qualifier("FinancialTransactionJPAIpml") FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<FinancialTransaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public FinancialTransaction findById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada con id: " + id));
    }

    @Override
    public FinancialTransaction create(FinancialTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public FinancialTransaction update(UUID id, FinancialTransaction transaction) {
        FinancialTransaction existing = findById(id);
        existing.setValue(transaction.getValue());
        existing.setDescription(transaction.getDescription());
        existing.setDate(transaction.getDate());
        existing.setStatus(transaction.getStatus());
        existing.setUpdatedAt(transaction.getUpdatedAt());
        existing.setAccount(transaction.getAccount());
        existing.setCategory(transaction.getCategory());

        return transactionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<FinancialTransaction> findByAccountId(UUID account_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByAccountId'");
    }
}
