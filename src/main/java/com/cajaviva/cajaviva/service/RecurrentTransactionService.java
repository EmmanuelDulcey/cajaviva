package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;

import java.util.List;
import java.util.UUID;

public interface RecurrentTransactionService {
    List<RecurrentTransaction> findAll();
    RecurrentTransaction findById(UUID id);
    RecurrentTransaction create(RecurrentTransaction transaction);
    RecurrentTransaction update(UUID id, RecurrentTransaction transaction);
    void delete(UUID id);

    List<RecurrentTransaction> findByAccount(Account account);
    List<RecurrentTransaction> findByCategory(Category category);
    List<RecurrentTransaction> findByStatus(Integer status);
    List<RecurrentTransaction> findByFrequency(Integer frequency);
    List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency);
}
