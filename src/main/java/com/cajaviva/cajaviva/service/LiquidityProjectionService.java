package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionService {
    List<LiquidityProjection> findAll();
    LiquidityProjection findById(UUID id);
    LiquidityProjection create(LiquidityProjection projection);
    LiquidityProjection update(UUID id, LiquidityProjection projection);
    void delete(UUID id);

    List<LiquidityProjection> findByAccount(Account account);
    List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate);
}
