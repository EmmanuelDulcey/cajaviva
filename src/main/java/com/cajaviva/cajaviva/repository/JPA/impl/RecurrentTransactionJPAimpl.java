package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.RecurrentTransactionDao;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.repository.JPA.RecurrentTransactionRepository;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("RecurrentTransactionJPAIpml")
public class RecurrentTransactionJPAimpl implements RecurrentTransactionDao {

    private final RecurrentTransactionRepository recurrentTransactionRepository;

    public RecurrentTransactionJPAimpl(RecurrentTransactionRepository recurrentTransactionRepository) {
        this.recurrentTransactionRepository = recurrentTransactionRepository;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        return recurrentTransactionRepository.findAll();
    }

    @Override
    public Optional<RecurrentTransaction> findById(UUID id) {
        return recurrentTransactionRepository.findById(id);
    }

    @Override
    public RecurrentTransaction save(RecurrentTransaction entity) {
        return recurrentTransactionRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return recurrentTransactionRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
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
    public List<RecurrentTransaction> findByFrequency(Integer frequency) {
        return recurrentTransactionRepository.findByFrequency(frequency);
    }
        @Override
    public List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency) {
        return recurrentTransactionRepository.findByCustomFrequency(customFrequency);
    }

    @Override
    public List<RecurrentTransaction> findByStatus(Integer status) {
        return recurrentTransactionRepository.findByStatus(status);
    }

    @Override
    public List<RecurrentTransaction> findByAccount(UUID account_id) {
        return recurrentTransactionRepository.findByAccount_Id(account_id);
    }

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID account_id) {
        return recurrentTransactionRepository.findByAccount_Id(account_id);
    }

    @Override
    public List<RecurrentTransaction> findByCategoryId(UUID categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return recurrentTransactionRepository.findByCategory(category);
    }

    @Override
    public List<User> findByActive(boolean active) {
        return List.of();
    }

}
