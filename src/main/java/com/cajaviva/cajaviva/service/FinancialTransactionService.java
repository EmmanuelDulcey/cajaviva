package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FinancialTransactionService {

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    public FinancialTransaction save(FinancialTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<FinancialTransaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<FinancialTransaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }
}