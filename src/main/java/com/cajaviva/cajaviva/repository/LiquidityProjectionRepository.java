package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionRepository extends JpaRepository<LiquidityProjection, UUID> {
    List<LiquidityProjection> findByAccount(Account account);
    List<LiquidityProjection> findByProjectionDate(java.time.LocalDate projectionDate);
}
