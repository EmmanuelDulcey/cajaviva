package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.FinancialTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionService {
    List<FinancialTransaction> findAll();
    FinancialTransaction findById(UUID id);
    FinancialTransaction create(FinancialTransaction transaction);
    FinancialTransaction update(UUID id, FinancialTransaction transaction);
    void delete(UUID id);
}
