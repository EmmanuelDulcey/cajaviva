package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.dao.jpa.LiquidityProjectionJpaRepository;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LiquidityProjectionDaoImpl implements LiquidityProjectionDao {

    private final LiquidityProjectionJpaRepository liquidityProjectionJpaRepository;

    public LiquidityProjectionDaoImpl(LiquidityProjectionJpaRepository liquidityProjectionJpaRepository) {
        this.liquidityProjectionJpaRepository = liquidityProjectionJpaRepository;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        return liquidityProjectionJpaRepository.findAll();
    }

    @Override
    public Optional<LiquidityProjection> findById(UUID id) {
        return liquidityProjectionJpaRepository.findById(id);
    }

    @Override
    public LiquidityProjection save(LiquidityProjection entity) {
        return liquidityProjectionJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return liquidityProjectionJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        liquidityProjectionJpaRepository.deleteById(id);
    }

    @Override
    public List<LiquidityProjection> findByAccountId(UUID account_id) {
        return liquidityProjectionJpaRepository.findByAccountId(account_id);
    }
}
