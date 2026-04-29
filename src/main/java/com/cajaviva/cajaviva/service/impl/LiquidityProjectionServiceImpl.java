package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.service.LiquidityProjectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LiquidityProjectionServiceImpl implements LiquidityProjectionService {

    private final LiquidityProjectionRepository liquidityProjectionRepository;

    public LiquidityProjectionServiceImpl(LiquidityProjectionRepository liquidityProjectionRepository) {
        this.liquidityProjectionRepository = liquidityProjectionRepository;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        return liquidityProjectionRepository.findAll();
    }

    @Override
    public LiquidityProjection findById(UUID id) {
        return liquidityProjectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyección de liquidez no encontrada con id: " + id));
    }

    @Override
    public LiquidityProjection create(LiquidityProjection projection) {
        return liquidityProjectionRepository.save(projection);
    }

    @Override
    public LiquidityProjection update(UUID id, LiquidityProjection projection) {
        LiquidityProjection existing = findById(id);
        existing.setAccount(projection.getAccount());
        existing.setProjectionDate(projection.getProjectionDate());
        existing.setAmount(projection.getAmount());
        existing.setUpdatedAt(projection.getUpdatedAt());

        return liquidityProjectionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        liquidityProjectionRepository.deleteById(id);
    }

    @Override
    public List<LiquidityProjection> findByAccount(Account account) {
        return liquidityProjectionRepository.findByAccount(account);
    }

    @Override
    public List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate) {
        return liquidityProjectionRepository.findByProjectionDate(projectionDate);
    }
}
