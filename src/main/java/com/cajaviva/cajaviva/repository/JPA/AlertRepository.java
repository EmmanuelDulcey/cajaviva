package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("AlertJPAImpl")   // 👈 este nombre debe coincidir con el @Qualifier
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByStatus(Integer status);
    List<Alert> findByType(Integer type);
    List<Alert> findByLiquidityProjectionId(UUID liquidityProjectionId);
}
