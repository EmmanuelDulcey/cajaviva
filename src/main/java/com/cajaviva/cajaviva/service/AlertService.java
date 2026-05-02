package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Alert;
import java.util.List;
import java.util.UUID;

public interface AlertService {
    List<Alert> findAll();
    Alert findById(UUID id);
    Alert create(Alert alert);
    Alert update(UUID id, Alert alert);
    void delete(UUID id);
    List<Alert> findByLiquidityProjectionId(UUID projection_id);
}
