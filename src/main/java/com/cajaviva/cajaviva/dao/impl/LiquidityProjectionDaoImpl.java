package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.repository.LiquidityProjectionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LiquidityProjectionDaoImpl implements LiquidityProjectionDao {

    private final LiquidityProjectionRepository projectionRepository;

    public LiquidityProjectionDaoImpl(LiquidityProjectionRepository projectionRepository) {
        this.projectionRepository = projectionRepository;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        return projectionRepository.findAll();
    }

    @Override
    public Optional<LiquidityProjection> findById(UUID id) {
        return projectionRepository.findById(id);
    }

    @Override
    public LiquidityProjection save(LiquidityProjection entity) {
        return projectionRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return projectionRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        projectionRepository.deleteById(id);
    }

    @Override
    public List findByAccount(Account account) {
        return projectionRepository.findByAccount(account);
    }

    @Override
    public List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate) {
        return projectionRepository.findByProjectionDate(projectionDate);
    }

    @Override
    public List<FinancialTransaction> findByStatus(Integer status) {
        throw new UnsupportedOperationException("Unimplemented method 'findByStatus'");
    }

    @Override
    public List<LiquidityProjection> findByAccountId(UUID account_id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByAccountId'");
    }
}
