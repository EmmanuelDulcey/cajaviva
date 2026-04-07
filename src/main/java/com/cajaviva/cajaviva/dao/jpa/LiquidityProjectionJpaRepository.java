package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionJpaRepository extends JpaRepository<LiquidityProjection, UUID> {

    List<LiquidityProjection> findByAccountId(UUID accountId);
}
