package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.repository.JPA.AlertRepository;
import com.cajaviva.cajaviva.service.AlertService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service 
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(@Qualifier ("AlertJPAImpl") AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    @Override
    public Alert findById(UUID id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con id: " + id));
    }

    @Override
    public Alert create(Alert alert) {
        return alertRepository.save(alert);
    }

    @Override
    public Alert update(UUID id, Alert alert) {
        Alert existing = findById(id);
        existing.setType(alert.getType());
        existing.setMessage(alert.getMessage());
        existing.setDate(alert.getDate());
        existing.setStatus(alert.getStatus());
        existing.setUpdatedAt(alert.getUpdatedAt());
        existing.setLiquidityProjection(alert.getLiquidityProjection());

        return alertRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        alertRepository.deleteById(id);
    }

    @Override
    public List<Alert> findByLiquidityProjectionId(UUID projection_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByLiquidityProjectionId'");
    }
}
