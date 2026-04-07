package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.RecurrentTransactionDao;
import com.cajaviva.cajaviva.dao.jpa.RecurrentTransactionJpaRepository;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecurrentTransactionDaoImpl implements RecurrentTransactionDao {

    private final RecurrentTransactionJpaRepository recurrentTransactionJpaRepository;

    public RecurrentTransactionDaoImpl(RecurrentTransactionJpaRepository recurrentTransactionJpaRepository) {
        this.recurrentTransactionJpaRepository = recurrentTransactionJpaRepository;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        return recurrentTransactionJpaRepository.findAll();
    }

    @Override
    public Optional<RecurrentTransaction> findById(UUID id) {
        return recurrentTransactionJpaRepository.findById(id);
    }

    @Override
    public RecurrentTransaction save(RecurrentTransaction entity) {
        return recurrentTransactionJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return recurrentTransactionJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        recurrentTransactionJpaRepository.deleteById(id);
    }

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID account_id) {
        return recurrentTransactionJpaRepository.findByAccountId(account_id);
    }
}
