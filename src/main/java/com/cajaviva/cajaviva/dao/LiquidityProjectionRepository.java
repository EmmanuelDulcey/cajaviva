package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LiquidityProjectionRepository extends JpaRepository<LiquidityProjection, UUID> {
    List<LiquidityProjection> findByAccountId(UUID accountId);
}
