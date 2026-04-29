package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionDao extends BaseDao<LiquidityProjection, UUID> {

    List<LiquidityProjection> findByAccount(Account account);
    List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate);
    List<FinancialTransaction> findByStatus(Integer status);
    List<LiquidityProjection> findByAccountId(UUID account_id);
}
