package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {

    // Buscar alertas por estado
    List<Alert> findByStatus(Integer status);

    // Buscar alertas por tipo
    List<Alert> findByType(Integer type);

    // Buscar alertas por LiquidityProjection
    List<Alert> findByLiquidityProjection(LiquidityProjection liquidityProjection);
}
