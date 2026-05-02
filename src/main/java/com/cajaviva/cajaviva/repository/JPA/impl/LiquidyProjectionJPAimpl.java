package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("LiquidityProjectionJPAImpl")
public class LiquidyProjectionJPAimpl implements LiquidityProjectionDao {

    private final LiquidityProjectionRepository projectionRepository;

    public LiquidyProjectionJPAimpl(LiquidityProjectionRepository projectionRepository) {
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

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }
}
