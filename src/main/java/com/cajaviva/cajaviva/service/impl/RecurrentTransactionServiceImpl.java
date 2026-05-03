package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.repository.JPA.RecurrentTransactionRepository;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.service.RecurrentTransactionService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecurrentTransactionServiceImpl implements RecurrentTransactionService {

    private final RecurrentTransactionRepository recurrentTransactionRepository;

    public RecurrentTransactionServiceImpl(RecurrentTransactionRepository recurrentTransactionRepository) {
        this.recurrentTransactionRepository = recurrentTransactionRepository;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        return recurrentTransactionRepository.findAll();
    }

    @Override
    public RecurrentTransaction findById(UUID id) {
        return recurrentTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción recurrente no encontrada con id: " + id));
    }

    @Override
    public RecurrentTransaction create(RecurrentTransaction transaction) {
        return recurrentTransactionRepository.save(transaction);
    }

    @Override
    public RecurrentTransaction update(UUID id, RecurrentTransaction transaction) {
        RecurrentTransaction existing = findById(id);
        existing.setAccount(transaction.getAccount());
        existing.setCategory(transaction.getCategory());
        existing.setAmount(transaction.getAmount());
        existing.setFrequency(transaction.getFrequency());
        existing.setCustomFrequency(transaction.getCustomFrequency());
        existing.setStatus(transaction.getStatus());
        existing.setUpdatedAt(transaction.getUpdatedAt());

        return recurrentTransactionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        recurrentTransactionRepository.deleteById(id);
    }

    @Override
    public List<RecurrentTransaction> findByAccount(Account account) {
        return recurrentTransactionRepository.findByAccount(account);
    }

    @Override
    public List<RecurrentTransaction> findByCategory(Category category) {
        return recurrentTransactionRepository.findByCategory(category);
    }

    @Override
    public List<RecurrentTransaction> findByStatus(Integer status) {
        return recurrentTransactionRepository.findByStatus(status);
    }

    @Override
    public List<RecurrentTransaction> findByFrequency(Integer frequency) {
        return recurrentTransactionRepository.findByFrequency(frequency);
    }

    @Override
    public List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency) {
        return recurrentTransactionRepository.findByCustomFrequency(customFrequency);
    }

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID account_id) {
        return recurrentTransactionRepository.findByAccount_Id(account_id);
    }
}
