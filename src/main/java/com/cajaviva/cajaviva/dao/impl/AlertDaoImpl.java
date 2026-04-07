package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.AlertDao;
import com.cajaviva.cajaviva.dao.jpa.AlertJpaRepository;
import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AlertDaoImpl implements AlertDao {

    private final AlertJpaRepository alertJpaRepository;

    public AlertDaoImpl(AlertJpaRepository alertJpaRepository) {
        this.alertJpaRepository = alertJpaRepository;
    }

    @Override
    public List<Alert> findAll() {
        return alertJpaRepository.findAll();
    }

    @Override
    public Optional<Alert> findById(UUID id) {
        return alertJpaRepository.findById(id);
    }

    @Override
    public Alert save(Alert entity) {
        return alertJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return alertJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        alertJpaRepository.deleteById(id);
    }

    @Override
    public List<Alert> findByLiquidityProjectionId(UUID liquidity_projection_id) {
        return alertJpaRepository.findByLiquidityProjectionId(liquidity_projection_id);
    }
}
