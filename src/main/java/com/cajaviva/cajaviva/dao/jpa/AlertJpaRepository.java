package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByLiquidityProjectionId(UUID liquidityProjectionId);
}
