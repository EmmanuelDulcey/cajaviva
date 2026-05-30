package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionService {
    /**
     * Calcula la proyección de liquidez en base a las transacciones del usuario autenticado, para una cuenta y rango de fechas dados.
     * @param accountId id de la cuenta
     * @param startDate fecha inicial (no puede ser pasada)
     * @param endDate fecha final (>= startDate)
     * @return Lista de proyecciones líquidas diarias
     */
    List<LiquidityProjection> calculateProjection(UUID accountId, LocalDate startDate, LocalDate endDate);

    List<LiquidityProjection> findAll();
    LiquidityProjection findById(UUID id);
    LiquidityProjection create(LiquidityProjection projection);
    LiquidityProjection update(UUID id, LiquidityProjection projection);
    void delete(UUID id);

    List<LiquidityProjection> findByAccount(Account account);
    List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate);
    List<LiquidityProjection> findByAccountId(UUID account_id);
    List<LiquidityProjection> findByUserId(UUID userId);
}
